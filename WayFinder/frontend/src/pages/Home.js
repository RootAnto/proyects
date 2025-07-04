import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import '../styles/Home.css';
import Header from '../components/Header';
import Footer from '../components/Footer';
import LoadingSpinner from '../components/LoadingSpinner';

const Toast = ({ message, onClose }) => (
  <div className="toast" role="alert" aria-live="assertive" aria-atomic="true">
    <span>{message}</span>
    <button onClick={onClose} aria-label="Cerrar notificación">✖</button>
  </div>
);

const ChatBotWidget = () => {
  const [open, setOpen] = useState(false);
  const [mensaje, setMensaje] = useState("");
  const [alerta, setAlerta] = useState("");
  const { currentUser } = useAuth();

  const iniciarChat = () => {
    if (!currentUser) return;
    const payload = {
      email: currentUser.email,
      nombre: currentUser.displayName || currentUser.name || "Usuario"
    };
    fetch('http://localhost:8000/abrir-chat-multifase', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    })
      .then(res => res.json())
      .then(data => setMensaje(data.status))
      .catch(err => console.error("Error en fetch:", err));
  };

  const toggleChat = () => {
    if (!currentUser) {
      setAlerta("Debes iniciar sesión para usar el asistente virtual.");
      setTimeout(() => setAlerta(""), 4000);
      return;
    }

    if (!open) iniciarChat();
    setOpen(!open);
  };

  return (
    <>
      <button
        className="chatbot-button"
        onClick={toggleChat}
        aria-label="Abrir asistente virtual"
        aria-expanded={open}
        aria-haspopup="dialog"
        title="¿Necesitas ayuda?"
      >
        💬
      </button>
      {alerta && (
        <div className="alerta-chatbot" role="alert" aria-live="assertive">
          {alerta}
        </div>
      )}
      {open && <div>{mensaje}</div>}
    </>
  );
};

function App() {
  const navigate = useNavigate();
  const [mensaje, setMensaje] = useState('');
  const [toastMessage, setToastMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [searchParams, setSearchParams] = useState({
    from: 'Madrid',
    to: 'Barcelona',
    departure: '2025-06-20',
    return: '2025-06-25',
    passengers: '1 Adulto, Turista',
    nearbyFrom: false,
    nearbyTo: false,
    directOnly: false
  });

  const getCityCode = async (cityName) => {
    const res = await fetch(`http://localhost:8000/location-search?keyword=${encodeURIComponent(cityName)}`);
    const data = await res.json();
    return data.locations?.[0]?.iataCode || null;
  };

  useEffect(() => {
    fetch('http://localhost:8000/')
      .then(res => res.json())
      .then(data => setMensaje(data.message));
  }, []);

  const showToast = (msg) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(''), 5000);
  };

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setSearchParams({
      ...searchParams,
      [name]: type === 'checkbox' ? checked : value
    });
  };

  const handleSuggestTrip = async (e) => {
    e.preventDefault();
    setLoading(true);

    const originCode = await getCityCode(searchParams.from);
    const destinationCode = await getCityCode(searchParams.to);

    if (!originCode || !destinationCode) {
      showToast("No se encontraron códigos IATA.");
      setLoading(false);
      return;
    }

    try {
      const response = await fetch("http://localhost:8000/trip-search", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          originLocationCode: originCode,
          destinationLocationCode: destinationCode,
          departureDate: searchParams.departure,
          returnDate: searchParams.return || searchParams.departure,
          adults: 1,
          max: 1,
          cityCode: destinationCode,
          checkInDate: searchParams.departure,
          checkOutDate: searchParams.return || searchParams.departure,
          hotelLimit: 1,
          defaultHotelPrice: 100,
          vehicleLocation: destinationCode,
          vehicleLimit: 1
        })
      });

      if (!response.ok) throw new Error('Error al obtener sugerencia de viaje');
      const data = await response.json();

      const updatedSearchParams = {
        ...searchParams,
        from: originCode,
        to: destinationCode
      };

      navigate('/TripSuggestion', {
        state: { searchParams: updatedSearchParams, tripData: data }
      });

    } catch (error) {
      showToast(error.message || 'Error al obtener sugerencia');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    const originCode = await getCityCode(searchParams.from);
    const destinationCode = await getCityCode(searchParams.to);

    if (!originCode || !destinationCode) {
      showToast("No se encontraron códigos IATA.");
      setLoading(false);
      return;
    }

    try {
      const [flightsResponse, hotelsResponse, vehiclesResponse] = await Promise.all([
        fetch("http://localhost:8000/flight-search", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            originLocationCode: originCode,
            destinationLocationCode: destinationCode,
            departureDate: searchParams.departure,
            returnDate: searchParams.return,
            adults: 1,
            max: 5
          })
        }),
        fetch("http://localhost:8000/hotel-search", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            cityCode: destinationCode,
            radius: 10,
            checkInDate: searchParams.departure,
            checkOutDate: searchParams.return || searchParams.departure,
            limit: 10,
            defaultPrice: 100
          })
        }),
        fetch("http://localhost:8000/vehicle-search", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            location: destinationCode,
            pickUpDate: searchParams.departure,
            dropOffDate: searchParams.return || searchParams.departure,
            vehicleType: "economy",
            limit: 10,
            defaultPrice: 50
          })
        })
      ]);

      if (!flightsResponse.ok || !hotelsResponse.ok || !vehiclesResponse.ok) {
        throw new Error('Error en una o más peticiones');
      }

      const [flightsData, hotelsData, vehiclesData] = await Promise.all([
        flightsResponse.json(),
        hotelsResponse.json(),
        vehiclesResponse.json()
      ]);

      const updatedSearchParams = {
        ...searchParams,
        from: originCode,
        to: destinationCode
      };

      navigate('/FlightResults', {
        state: {
          searchParams: updatedSearchParams,
          flightResults: flightsData.offers || [],
          hotelResults: hotelsData.results || [],
          vehicleResults: vehiclesData.cars || []
        }
      });

    } catch (error) {
      showToast(error.message || 'Error al realizar búsqueda');
    } finally {
      setLoading(false);
    }
  };

  const { currentUser } = useAuth();

  return (
    <div className="app">
      {loading && <LoadingSpinner message="Procesando tu solicitud..." />}

      {toastMessage && <Toast message={toastMessage} onClose={() => setToastMessage('')} />}

      <Header />

      <main className="main-content">
        <div className="container">
          <h1>Millones de vuelos baratos. Tú eliges tu próximo destino.</h1>
          <br />

          <SearchForm
            searchParams={searchParams}
            onInputChange={handleInputChange}
            onSubmit={handleSubmit}
            onSuggestTrip={handleSuggestTrip}
            loading={loading}
          />

          {mensaje && (
            <div className="alert alert-info" role="status" aria-live="polite">
              {mensaje}
            </div>
          )}

          {currentUser && (
            <div className="user-greeting">
              <p>Bienvenido, <strong>{currentUser.nombre}</strong>!</p>
            </div>
          )}
        </div>
      </main>

      <ChatBotWidget />
      <Footer />
    </div>
  );
}

const SearchForm = ({ searchParams, onInputChange, onSubmit, onSuggestTrip, loading }) => {
  const navigate = useNavigate();
  const handleGoToBookings = () => navigate('/my-bookings');

  return (
    <>
      <h2 id="search-form-title">Crea una ruta con múltiples destinos</h2>
      <form className="search-form" aria-labelledby="search-form-title">
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="from-input">Desde</label>
            <input
              id="from-input"
              type="text"
              name="from"
              value={searchParams.from}
              onChange={onInputChange}
              aria-required="true"
            />
          </div>
          <div className="form-group">
            <label htmlFor="to-input">A</label>
            <input
              id="to-input"
              type="text"
              name="to"
              value={searchParams.to}
              onChange={onInputChange}
              aria-required="true"
            />
          </div>
          <div className="form-group">
            <label htmlFor="departure-input">Ida</label>
            <input
              id="departure-input"
              type="date"
              name="departure"
              value={searchParams.departure}
              onChange={onInputChange}
              aria-required="true"
            />
          </div>
          <div className="form-group">
            <label htmlFor="return-input">Vuelta</label>
            <input
              id="return-input"
              type="date"
              name="return"
              value={searchParams.return}
              onChange={onInputChange}
            />
          </div>
          <div className="form-group">
            <label htmlFor="passengers-select">Viajeros y clase de cabina</label>
            <select
              id="passengers-select"
              name="passengers"
              value={searchParams.passengers}
              onChange={onInputChange}
              aria-required="true"
            >
              <option value="1 Adulto, Turista">1 Adulto, Turista</option>
              <option value="2 Adultos, Turista">2 Adultos, Turista</option>
              <option value="1 Adulto, Business">1 Adulto, Business</option>
              <option value="2 Adultos, Business">2 Adultos, Business</option>
              <option value="2 Adultos y 2 Niños, Turista">2 Adultos y 2 Niños, Turista</option>
              <option value="2 Adultos y 1 Niños, Turista">2 Adultos y 1 Niños, Turista</option>
            </select>
          </div>
        </div>

        <div className="form-row">
          <div style={{ flexGrow: 1, justifyContent: 'right', display: 'flex', gap: '10px' }}>
            <button type="button" disabled={loading} className="my-bookings-button" onClick={handleGoToBookings}>
              Mis reservas
            </button>
            <button type="button" disabled={loading} className="suggest-button" onClick={onSuggestTrip}>
              Sugerir viaje completo
            </button>
            <button type="submit" disabled={loading} className="search-button" onClick={onSubmit} aria-busy={loading}>
              {loading ? 'Buscando...' : 'Buscador'}
            </button>
          </div>
        </div>
      </form>
    </>
  );
};

export default App;
