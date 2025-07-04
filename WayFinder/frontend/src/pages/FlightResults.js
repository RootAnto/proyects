import { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import '../styles/FlightResults.css';
import Header from '../components/Header';
import Footer from '../components/Footer';

function FlightResults() {
  const { currentUser, logout } = useAuth();
  const { addToCart, cartItems } = useCart();
  const location = useLocation();
  const [activeTab, setActiveTab] = useState('flights');
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const {
    searchParams = {
      from: 'MAD',
      to: 'NYC',
      departure: '2025-06-15',
      return: '2025-06-25',
      passengers: '1 Adulto, Turista'
    },
    flightResults = [],
    hotelResults = [],
    vehicleResults = []
  } = location.state || {};
  const [flights, setFlights] = useState(flightResults);
  const [hotels, setHotels] = useState(hotelResults.data || hotelResults);
  const [vehicles, setVehicles] = useState(vehicleResults.data || vehicleResults);
  const [hotelImages, setHotelImages] = useState({});
  const [vehicleImages, setVehicleImages] = useState({});
  const [loading, setLoading] = useState({ flights: false, hotels: false, vehicles: false });
  const [error, setError] = useState(null);
  const [originCityName, setOriginCityName] = useState(searchParams.from);
  const [destinationCityName, setDestinationCityName] = useState(searchParams.to);

  const hasPackageInCart = () => {
    return cartItems.some(item => item.type === 'package');
  };

  useEffect(() => {
    const fetchCityName = async (code, setName) => {
      try {
        const res = await fetch(`http://localhost:8000/location-search?keyword=${code}`);
        const data = await res.json();
        if (data.locations && data.locations[0]) {
          setName(data.locations[0].address.cityName || code);
        }
      } catch {
        setName(code);
      }
    };

    fetchCityName(searchParams.from, setOriginCityName);
    fetchCityName(searchParams.to, setDestinationCityName);
  }, [searchParams.from, searchParams.to]);

  useEffect(() => {
    const fetchHotelImages = async () => {
      for (const hotel of hotels) {
        if (!hotelImages[hotel.hotelId]) {
          try {
            const response = await fetch(`http://localhost:8000/hotel-image/?query=${encodeURIComponent(hotel.name)}`);
            if (!response.ok) continue;
            const data = await response.json();
            setHotelImages(prev => ({ ...prev, [hotel.hotelId]: data.image_url }));
          } catch {}
        }
      }
    };
    if (hotels.length > 0) fetchHotelImages();
  }, [hotels]);

  useEffect(() => {
    const fetchVehicleImages = async () => {
      for (const vehicle of vehicles) {
        const key = `${vehicle.brand}-${vehicle.model}`;
        if (!vehicleImages[key]) {
          try {
            const response = await fetch(`http://localhost:8000/vehicle-image/?brand=${encodeURIComponent(vehicle.brand)}&model=${encodeURIComponent(vehicle.model)}`);
            if (!response.ok) continue;
            const data = await response.json();
            setVehicleImages(prev => ({ ...prev, [key]: data.image_url }));
          } catch {}
        }
      }
    };
    if (vehicles.length > 0) fetchVehicleImages();
  }, [vehicles]);

  const formatDate = (dateStr) =>
    new Date(dateStr).toLocaleDateString('es-ES', { weekday: 'short', day: 'numeric', month: 'short' });
  const formatTime = (dateTimeStr) =>
    new Date(dateTimeStr).toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' });
  const formatDuration = (duration) =>
    duration?.replace('PT', '').replace('H', 'h ').replace('M', 'm').trim();

  const fetchFlights = async () => {
    try {
      const res = await fetch('http://localhost:8000/flight-search', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          originLocationCode: searchParams.from,
          destinationLocationCode: searchParams.to,
          departureDate: searchParams.departure,
          returnDate: searchParams.return,
          adults: 1,
          max: 5
        })
      });
      if (!res.ok) throw new Error('Error al cargar vuelos');
      const data = await res.json();
      setFlights(data.offers || []);
    } catch (err) {
      setError(`Error en vuelos: ${err.message}`);
    } finally {
      setLoading(prev => ({ ...prev, flights: false }));
    }
  };

  const fetchHotels = async () => {
    try {
        const res = await fetch('http://localhost:8000/hotel-search', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            cityCode: searchParams.to,
            checkInDate: searchParams.departure,
            checkOutDate: searchParams.return || searchParams.departure,
            adults: 1,
            max: 10
          })
        });
        if (!res.ok) throw new Error('Error al cargar hoteles');
        const data = await res.json();
        const hotelsList = data.data || [];
        setHotels(hotelsList);

        for (const hotel of hotelsList) {
          const hotelQuery = `${hotel.name} hotel ${hotel.cityCode || ''}`.trim();
          try {
            const response = await fetch(`http://localhost:8000/hotel-image/?query=${encodeURIComponent(hotelQuery)}`);
            if (!response.ok) continue;
            const imgData = await response.json();
            setHotelImages(prev => ({ ...prev, [hotel.hotelId]: imgData.image_url }));
          } catch {}
        }

      } catch (err) {
        setError(`Error en hoteles: ${err.message}`);
      } finally {
        setLoading(prev => ({ ...prev, hotels: false }));
      }
    };

  const fetchVehicles = async () => {
    try {
      const res = await fetch('http://localhost:8000/vehicle-search', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          location: searchParams.to,
          pickUpDate: searchParams.departure,
          dropOffDate: searchParams.return || searchParams.departure,
          vehicleType: "economy",
          limit: 10,
          defaultPrice: 50
        })
      });
      if (!res.ok) throw new Error('Error al cargar vehículos');
      const data = await res.json();
      setVehicles(data.data || []);
    } catch (err) {
      setError(`Error en vehículos: ${err.message}`);
    } finally {
      setLoading(prev => ({ ...prev, vehicles: false }));
    }
  };

  useEffect(() => {
    if (activeTab === 'flights' && flights.length === 0) {
      setLoading(prev => ({ ...prev, flights: true }));
      fetchFlights();
    }
    if (activeTab === 'hotels' && hotels.length === 0) {
      setLoading(prev => ({ ...prev, hotels: true }));
      fetchHotels();
    }
    if (activeTab === 'vehicles' && vehicles.length === 0) {
      setLoading(prev => ({ ...prev, vehicles: true }));
      fetchVehicles();
    }
  }, [activeTab]);

  useEffect(() => {
    const handleClickOutside = (e) => {
      if (isMenuOpen && !e.target.closest('.user-menu-container')) {
        setIsMenuOpen(false);
      }
    };
    document.addEventListener('click', handleClickOutside);
    return () => document.removeEventListener('click', handleClickOutside);
  }, [isMenuOpen]);

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

  return (
    <div className="flight-results-app">
      <Header />

      <main className="results-container">
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

          <div role="tablist" aria-label="Resultados de búsqueda" className="results-navigation">
            <button
              role="tab"
              aria-selected={activeTab === 'flights'}
              aria-controls="flights-tab"
              id="flights-tab-btn"
              className={`nav-link ${activeTab === 'flights' ? 'active' : ''}`}
              onClick={() => setActiveTab('flights')}
            >
              Vuelos
            </button>
            <button
              role="tab"
              aria-selected={activeTab === 'hotels'}
              aria-controls="hotels-tab"
              id="hotels-tab-btn"
              className={`nav-link ${activeTab === 'hotels' ? 'active' : ''}`}
              onClick={() => setActiveTab('hotels')}
            >
              Hoteles
            </button>
            <button
              role="tab"
              aria-selected={activeTab === 'vehicles'}
              aria-controls="vehicles-tab"
              id="vehicles-tab-btn"
              className={`nav-link ${activeTab === 'vehicles' ? 'active' : ''}`}
              onClick={() => setActiveTab('vehicles')}
            >
              Vehículos
            </button>
          </div>

          <div className="search-summary">
            <h1>{originCityName} → {destinationCityName}</h1>
            <p>{formatDate(searchParams.departure)} - {searchParams.return && ` ${formatDate(searchParams.return)}`} | {searchParams.passengers}</p>
          </div>

          {error && <div className="error" role="alert">{error}</div>}

          <div className="tab-content">
            {activeTab === 'flights' && (
              <div 
                role="tabpanel"
                id="flights-tab"
                aria-labelledby="flights-tab-btn"
                className="flights-section"
              >
                {loading.flights ? (
                  <div className="loading" aria-live="polite" aria-busy="true">
                    Cargando vuelos...
                  </div>
                ) : flights.length === 0 ? (
                  <div className="no-results" aria-live="polite">
                    No se encontraron vuelos
                  </div>
                ) : (
                  <div className="flights-list">
                    {flights.map((flight, index) => {
                      const adultCount = getTotalPassengers();
                      const flightClass = getFlightClass();

                      let priceMultiplier = 1;
                      if (flightClass === 'business') {
                        priceMultiplier = 3;
                      }

                      const basePrice = flight?.price?.total ? parseFloat(flight.price.total) : 0;
                      const flightPrice = basePrice * adultCount * priceMultiplier;

                      const firstSegment = flight.itineraries[0].segments[0];
                      const lastSegment = flight.itineraries[0].segments.at(-1);

                      return (
                        <article key={index} className="flight-card">
                          <div className="flight-header">
                            <span className="airline">{firstSegment.carrierCode}</span>
                            <span className="price">
                              <span className="visually-hidden">{flightPrice.toFixed(2)} {flight.price.currency}</span>
                            </span>
                          </div>
                          <div className="flight-details">
                            <div className="time-block">
                              <time dateTime={firstSegment.departureTime} className="time">
                                {formatTime(firstSegment.departureTime)}
                              </time>
                              <span className="airport">{firstSegment.departureAirport}</span>
                            </div>
                            <div className="duration-block">
                              <div className="duration-line">
                                <span className="duration">
                                  {formatDuration(flight.itineraries[0].duration)}
                                </span>
                              </div>
                              <span className="stops">
                                {flight.itineraries[0].segments.length === 1 ? 'Directo' : `${flight.itineraries[0].segments.length - 1} escala(s)`}
                              </span>
                            </div>
                            <div className="time-block">
                              <time dateTime={lastSegment.arrivalTime} className="time">
                                {formatTime(lastSegment.arrivalTime)}
                              </time>
                              <span className="airport">{lastSegment.arrivalAirport}</span>
                            </div>
                          </div>
                          <div className="flight-footer">
                            <button 
                              className="select-btn"
                              onClick={() => {
                                if (hasPackageInCart()) {
                                  alert('Ya tienes un paquete en el carrito. No puedes añadir un vuelo individual.');
                                  return;
                                }
                                alert(`¡Vuelo agregado al carrito!`);
                                addToCart({
                                  type: 'flight',
                                  id: flight.id,
                                  airline: firstSegment.carrierCode,
                                  origin: searchParams.from,
                                  destination: searchParams.to,
                                  departure: searchParams.departure,
                                  returnDate: searchParams.return,
                                  price: flightPrice.toFixed(2),
                                  currency: flight.price.currency,
                                  duration: flight.itineraries[0].duration,
                                  passengers: searchParams.passengers
                                });
                              }}
                            >
                              Seleccionar
                            </button>
                          </div>
                        </article>
                      );
                    })}
                  </div>
                )}
              </div>
            )}

            {activeTab === 'hotels' && (
              <div 
                role="tabpanel"
                id="hotels-tab"
                aria-labelledby="hotels-tab-btn"
                className="hotels-section"
              >
                {loading.hotels ? (
                  <div className="loading" aria-live="polite" aria-busy="true">
                    Cargando hoteles...
                  </div>
                ) : hotels.length === 0 ? (
                  <div className="no-results" aria-live="polite">
                    No se encontraron hoteles
                  </div>
                ) : (
                  <div className="hotels-list">
                    {hotels.map((hotel) => (
                      <article key={hotel.hotelId} className="hotel-card">
                        <div className="hotel-image">
                          <img 
                            src={hotelImages[hotel.hotelId] || `https://source.unsplash.com/300x200/?hotel,${hotel.cityCode}`} 
                            alt={`Imagen del hotel ${hotel.name}`} 
                          />
                        </div>
                        <div className="hotel-info">
                          <h2>{hotel.name}</h2>
                          <p>{hotel.cityCode}</p>
                          <div>
                            <span className="visually-hidden">Precio: </span>
                            <strong>{hotel.price} {hotel.currency}</strong>
                          </div>
                        </div>
                        <button 
                          className="select-btn"
                          onClick={() => {
                            if (hasPackageInCart()) {
                              alert('Ya tienes un paquete en el carrito. No puedes añadir un hotel individual.');
                              return;
                            }
                            alert("¡Hotel agregado al carrito!");
                            addToCart({
                              type: 'hotel',
                              id: hotel.hotelId,
                              name: hotel.name,
                              city: hotel.cityCode,
                              price: hotel.price,
                              currency: hotel.currency
                            });
                          }}
                        >
                          Reservar
                        </button>
                      </article>
                    ))}
                  </div>
                )}
              </div>
            )}

            {activeTab === 'vehicles' && (
              <div 
                role="tabpanel"
                id="vehicles-tab"
                aria-labelledby="vehicles-tab-btn"
                className="vehicles-section"
              >
                {loading.vehicles ? (
                  <div className="loading" aria-live="polite" aria-busy="true">
                    Cargando vehículos...
                  </div>
                ) : vehicles.length === 0 ? (
                  <div className="no-results" aria-live="polite">
                    No se encontraron vehículos
                  </div>
                ) : (
                  <div className="vehicles-list">
                    {vehicles.map((vehicle) => (
                      <article key={vehicle.vehicleId} className="vehicle-card">
                        <div className="vehicle-image">
                          <img
                            src={vehicleImages[`${vehicle.brand}-${vehicle.model}`] || `https://source.unsplash.com/300x200/?car,${vehicle.brand}`}
                            alt={`Imagen del vehículo ${vehicle.brand} ${vehicle.model}`}
                          />
                        </div>
                        <div className="vehicle-info">
                          <h2>{vehicle.name} ({vehicle.year})</h2>
                          <div className="type">{vehicle.vehicleType || 'Economy'}</div>
                          <div>
                            <span className="visually-hidden">Precio: </span>
                            <strong>{vehicle.pricePerDay} {vehicle.currency} /día</strong>
                          </div>
                          <ul className="features" aria-label="Características del vehículo">
                            <li>{vehicle.seats} asientos</li>
                            <li>{vehicle.transmission}</li>
                            <li>{vehicle.fuelType}</li>
                            <li>{vehicle.doors} puertas</li>
                          </ul>
                        </div>
                        <button 
                          className="select-btn"
                          onClick={() => {
                            const pickUp = new Date(searchParams.departure);
                            const dropOff = new Date(searchParams.return || searchParams.departure);
                            const days = Math.ceil((dropOff - pickUp) / (1000 * 60 * 60 * 24));
                            if (hasPackageInCart()) {
                              alert('Ya tienes un paquete en el carrito. No puedes añadir un vehículo individual.');
                              return;
                            }
                            alert("Vehículo agregado al carrito!");
                            addToCart({
                              type: 'vehicle',
                              vehicleId: vehicle.vehicleId,
                              name: vehicle.name,
                              model: vehicle.name,
                              brand: vehicle.brand,
                              price: vehicle.pricePerDay * days,
                              pricePerDay: vehicle.pricePerDay,
                              currency: vehicle.currency,
                              days: days,
                              vehicleType: vehicle.vehicleType,
                              pickUpDate: searchParams.departure,
                              dropOffDate: searchParams.return || searchParams.departure
                            });
                          }}
                        >
                          Alquilar
                        </button>
                      </article>
                    ))}
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      </main>

      <Footer />
    </div>
  );
}

export default FlightResults;