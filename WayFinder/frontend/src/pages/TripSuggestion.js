import { useState, useEffect } from 'react';
import { useLocation, Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import Header from '../components/Header';
import Footer from '../components/Footer';
import '../styles/TripSuggestion.css';

function TripSuggestion() {
  const { currentUser } = useAuth();
  const { addToCart, cartItems } = useCart();
  const location = useLocation();
  const navigate = useNavigate();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');  
  const {
    searchParams = {
      from: 'MAD',
      to: 'NYC',
      departure: '2025-06-15',
      return: '2025-06-25',
      passengers: '1 Adulto, Turista'
    },
    tripData
  } = location.state || {};
  const [originCityName, setOriginCityName] = useState(searchParams.from);
  const [destinationCityName, setDestinationCityName] = useState(searchParams.to);

  useEffect(() => {
    const fetchCityName = async (code, setName) => {
      try {
        const res = await fetch(`http://localhost:8000/location-search?keyword=${code}`);
        const data = await res.json();
        if (data.locations && data.locations[0]) {
          setName(data.locations[0].address.cityName || code);
        }
      } catch {
        setName(code); // fallback
      }
    };

    fetchCityName(searchParams.from, setOriginCityName);
    fetchCityName(searchParams.to, setDestinationCityName);
  }, [searchParams.from, searchParams.to]);


  useEffect(() => {
    const handleClickOutside = (e) => {
      if (isMenuOpen && !e.target.closest('.user-menu-container')) {
        setIsMenuOpen(false);
      }
    };
    document.addEventListener('click', handleClickOutside);
    return () => document.removeEventListener('click', handleClickOutside);
  }, [isMenuOpen]);


  const formatDate = (dateStr) => {
    if (!dateStr) return '';
    const options = { weekday: 'short', day: 'numeric', month: 'short' };
    return new Date(dateStr).toLocaleDateString('es-ES', options);
  };

  const formatTime = (dateTimeStr) => {
    if (!dateTimeStr) return '';
    return new Date(dateTimeStr).toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' });
  };

  const formatDuration = (duration) => {
    if (!duration) return '';
    return duration.replace('PT', '').replace('H', 'h ').replace('M', 'm').trim();
  };

  const handleBookTrip = async () => {
    setErrorMessage('');

    if (!currentUser) {
      setErrorMessage("Debes iniciar sesión para reservar un viaje.");
      return;
    }

    const hasIndividualItems = cartItems?.some(item => !item.isPackage);
    if (hasIndividualItems) {
      setErrorMessage("No puedes añadir un paquete si ya tienes vuelos, hoteles o coches individuales en el carrito.");
      return;
    }

    setLoading(true);
    try {
      const flight = tripData.flights?.[0];
      const hotel = tripData.hotels?.[0];
      const vehicle = tripData.vehicles?.[0];

      const departure = new Date(searchParams.departure);
      const returnDate = searchParams.return ? new Date(searchParams.return) : null;

      const hotelNights = hotel?.nights || (
        returnDate
          ? Math.ceil((returnDate - departure) / (1000 * 60 * 60 * 24))
          : 1
      );

      const vehicleDays = hotelNights;

      const adultCount = getTotalPassengers();
      const flightClass = getFlightClass();

      let priceMultiplier = 1;
      if (flightClass === 'business') {
        priceMultiplier = 3;
      }

      const basePrice = flight?.price?.total ? parseFloat(flight.price.total) : 0;
      const flightPrice = basePrice * adultCount * priceMultiplier;
      const hotelPrice = typeof hotel?.price === "number" ? hotel.price : 0;
      const vehiclePrice = typeof vehicle?.pricePerDay === "number" && vehicleDays ? parseFloat((vehicle.pricePerDay * vehicleDays).toFixed(2)) : 0;

      const packageItem = {
        id: `package-${Date.now()}`,
        type: 'package',
        name: `Paquete Completo: ${searchParams.from} → ${searchParams.to}`,
        price: parseFloat((flightPrice + hotelPrice + vehiclePrice).toFixed(2)),
        currency: flight?.price?.currency || hotel?.currency || vehicle?.currency || "EUR",
        passengers: searchParams.passengers,
        details: {
          flight: {
            id: flight?.id,
            airline: flight?.itineraries[0]?.segments[0]?.carrierCode,
            origin: searchParams.from,
            destination: searchParams.to,
            departure: searchParams.departure,
            returnDate: searchParams.return || null,
            price: flightPrice
          },
          hotel: {
            id: hotel?.hotelId,
            name: hotel?.name,
            nights: hotelNights,
            price: hotelPrice,
            checkIn: searchParams.departure,
            checkOut: searchParams.return || searchParams.departure
          },
          vehicle: vehicle ? {
            id: vehicle?.vehicleId,
            model: vehicle?.name,
            days: vehicleDays,
            price: vehiclePrice,
            pickUpDate: searchParams.departure,
            dropOffDate: searchParams.return || searchParams.departure
          } : null
        },
        isPackage: true
      };

      addToCart(packageItem);
      alert("Se ha añadido el paquete al carrito");
    } catch (error) {
      setErrorMessage('Error al reservar el paquete. Inténtalo de nuevo.');
    } finally {
      setLoading(false);
    }
  };

  if (!tripData) {
    return (
      <div className="flight-results-app">
        <Header />
        <main className="results-container">
          <div className="container">
            <nav aria-label="Migas de pan" className="breadcrumb">
              <ol>
                <li><Link to="/">Inicio</Link></li>
                <li aria-current="page">Sugerencia de viaje</li>
              </ol>
            </nav>
            <div className="error" role="alert">
              No se encontraron datos de viaje. Por favor, inténtalo de nuevo.
            </div>
          </div>
        </main>
        <Footer />
      </div>
    );
  }

  const getTotalPassengers = () => {
    const passengers = searchParams.passengers;

    const adultMatch = passengers.match(/(\d+)\s*Adulto[s]?/i);
    const adults = adultMatch ? parseInt(adultMatch[1]) : 0;

    const childMatch = passengers.match(/(\d+)\s*Niñ[oa]s?/i);
    const children = childMatch ? parseInt(childMatch[1]) : 0;

    return adults + children;
  };

  const getFlightClass = () => {
    const parts = searchParams.passengers.split(',').map(s => s.trim().toLowerCase());
    if (parts.some(p => p.includes('business'))) return 'business';
    if (parts.some(p => p.includes('turista'))) return 'economy';
    return 'economy';
  };

  const flight = tripData.flights?.[0];
  const hotel = tripData.hotels?.[0];
  const vehicle = tripData.vehicles?.[0];

  const departure = new Date(searchParams.departure);
  const returnDate = searchParams.return ? new Date(searchParams.return) : null;

  const hotelNights = hotel?.nights || (returnDate ? Math.ceil((returnDate - departure) / (1000 * 60 * 60 * 24)) : 1);
  const vehicleDays = hotelNights;

  const adultCount = getTotalPassengers();
  const flightClass = getFlightClass();

  let priceMultiplier = 1;
  if (flightClass === 'business') {
    priceMultiplier = 3;
  }

  const basePrice = flight?.price?.total ? parseFloat(flight.price.total) : 0;
  const flightPrice = basePrice * adultCount * priceMultiplier;
  const hotelPrice = typeof hotel?.price === "number" ? hotel.price : 0;
  const vehiclePrice = vehicle?.pricePerDay ? vehicle.pricePerDay * vehicleDays : 0;

  const currency = flight?.price?.currency || hotel?.currency || vehicle?.currency || "EUR";

  return (
    <div className="flight-results-app">
      <Header />
      {loading && (
        <div 
          className="loading-overlay" 
          role="alert" 
          aria-live="assertive"
          aria-busy="true"
        >
          <div className="spinner" aria-hidden="true" />
          <p>Procesando tu reserva...</p>
        </div>
      )}
      <main 
        className="results-container" 
        style={{ opacity: loading ? 0.5 : 1, pointerEvents: loading ? 'none' : 'auto' }}
        aria-hidden={loading}
      >
        <div className="container">
          <div className="breadcrumb">
            <div><Link to="/">Inicio</Link> &gt; <span>Sugerencia de viaje completo</span></div>
            <div className="cart-icon">
              <Link to="/cart">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <circle cx="9" cy="21" r="1"></circle>
                  <circle cx="20" cy="21" r="1"></circle>
                  <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
                </svg>
              </Link>
            </div>
          </div>

          <div className="search-summary">
            <h1>{originCityName} → {destinationCityName}</h1>
            <p>
              <time dateTime={searchParams.departure}>{formatDate(searchParams.departure)}</time> -
              {searchParams.return && (
                <>
                  {' '}
                  <time dateTime={searchParams.return}>{formatDate(searchParams.return)}</time>
                </>
              )} | {searchParams.passengers}
            </p>
          </div>

          <article className="flight-card">
            <h2>Vuelo seleccionado</h2>
            {flight && (
              <>
                <div className="flight-header">
                  <span className="airline">{flight.itineraries[0].segments[0].carrierCode}</span>
                  <span className="price">
                    {flightPrice} {flight.price.currency}
                  </span>
                </div>
                <div className="flight-details">
                  <div className="time-block">
                    <time dateTime={flight.itineraries[0].segments[0].departureTime} className="time">
                      {formatTime(flight.itineraries[0].segments[0].departureTime)}
                    </time>
                    <span className="airport">{flight.itineraries[0].segments[0].departureAirport}</span>
                  </div>
                  <div className="duration-block">
                    <div className="duration-line">
                      <span className="duration">{formatDuration(flight.itineraries[0].duration)}</span>
                    </div>
                    <span className="stops">
                      {flight.itineraries[0].segments.length === 1 ? 'Directo' : `${flight.itineraries[0].segments.length - 1} escala(s)`}
                    </span>
                  </div>
                  <div className="time-block">
                    <time dateTime={flight.itineraries[0].segments.slice(-1)[0].arrivalTime} className="time">
                      {formatTime(flight.itineraries[0].segments.slice(-1)[0].arrivalTime)}
                    </time>
                    <span className="airport">{flight.itineraries[0].segments.slice(-1)[0].arrivalAirport}</span>
                  </div>
                </div>
              </>
            )}
          </article>

          <article className="hotel-card">
            {hotel && (
              <>
                <div className="hotel-header">
                  <h2>Hotel seleccionado</h2>
                  <span className="price">{hotel.price} EUR</span>
                </div>
                <p>
                  <span className="visually-hidden">Hotel: </span>
                  {hotel.name}
                </p>
                <p>Noches: {hotelNights}</p>
                <p>Precio por noche: {hotel.price / 10}</p>
              </>
            )}
          </article>

          {vehicle && (
            <article className="vehicle-card">
              <div className="hotel-header">
                <h2>Coche de alquiler</h2>
                <span className="price">{(vehicle.pricePerDay * vehicleDays).toFixed(2)} EUR</span>
              </div>
              <p>
                <span className="visually-hidden">Modelo: </span>
                {vehicle.name}
              </p>
              <p>
                <span className="visually-hidden">Días: {vehicleDays}</span>
                
              </p>
              <p>
                <span className="visually-hidden">Precio por día: </span>
                {vehicle.pricePerDay} EUR
              </p>
            </article>
          )}

          <article className="summary-card">
            <div className="hotel-header">
              <h2>Precio total del paquete</h2>
              <p>
                <span className="price">{(flightPrice + hotelPrice + vehiclePrice).toFixed(2)} {currency}</span>
              </p>
            </div>
            <p>
              *Este precio no incluye impuestos y tasas que se aplicarán en el proceso de reserva
            </p>
          </article>

          <div className="div-select-btn">
            <button
              className="select-btn"
              onClick={handleBookTrip}
              disabled={loading}
              aria-busy={loading}
            >
              {loading ? 'Reservando...' : 'Reservar paquete completo'}
            </button>
          </div>
          {errorMessage && (
            <div className="error-message" role="alert">
              <strong>{errorMessage}</strong>
              {cartItems?.some(item => !item.isPackage) && (
                <div className="package-warning">
                  <p style={{ marginTop: '10px' }}>
                    Ya tienes elementos individuales (vuelo, hotel o coche) en el carrito. Elimina esos elementos antes de añadir un paquete.
                  </p>
                  <Link to="/cart" className="go-to-cart-link">
                    <button className="go-to-cart-btn">Ir al carrito</button>
                  </Link>
                </div>
              )}
            </div>
          )}
        </div>
      </main>
      <Footer />
    </div>
  );
}

export default TripSuggestion;