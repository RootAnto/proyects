import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import '../styles/Cart.css';
import { FaTrash, FaPlane, FaHotel, FaCar, FaLock, FaBox } from 'react-icons/fa';
import SuccessModal from '../components/SuccessModal';
import LoadingSpinner from '../components/LoadingSpinner';

export default function CartPage() {
  const { cartItems, removeFromCart, clearCart } = useCart();
  const { currentUser } = useAuth();
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const calculateTotal = () => {
    return cartItems.reduce((acc, item) => acc + Number(item.price), 0);
  };

  // Cálculo de totales
  const subtotal = cartItems.reduce((sum, item) => sum + Number(item.price), 0);
  const tax = subtotal * 0.21;
  const total = subtotal + tax;

  function extractPassengerCounts(passengerString = '') {
    const adultMatch = passengerString.match(/(\d+)\s*Adult[oa]s?/i);
    const childMatch = passengerString.match(/(\d+)\s*Niñ[oa]s?/i);

    const adults = adultMatch ? parseInt(adultMatch[1], 10) : 1; // mínimo 1
    const children = childMatch ? parseInt(childMatch[1], 10) : 0;

    return { adults, children };
  }

  const buildTripPayload = ({
    user,
    flight,
    hotel,
    vehicle,
    isPackage = false,
    totalPrice = 0,
    currency = 'EUR',
    passengers = '',
  }) => {
    const departure = new Date(flight.departure);
    const returnDate = flight.returnDate ? new Date(flight.returnDate) : null;
    const hotelNights = hotel.nights || (returnDate ? Math.ceil((returnDate - departure) / (1000 * 60 * 60 * 24)) : 1);
    const vehicleDays = vehicle?.days || hotelNights;

    const { adults, children } = extractPassengerCounts(passengers);

    return {
      user_id: String(user.id),
      origin: flight.origin,
      destination: flight.destination,
      departure_date: flight.departure,
      return_date: flight.returnDate || null,
      adults,
      children,
      user_email: user.email || '',
      user_name: user.nombre || user.name || '',

      flight_id: String(flight.id || ''),
      hotel_id: String(hotel.id || ''),
      vehicle_id: vehicle ? String(vehicle.id) : '',

      flight_price: parseFloat(flight.price),
      flight_name: flight.airline || flight.name || '',

      hotel_name: hotel.name || '',
      hotel_price: parseFloat(hotel.price),
      hotel_nights: hotelNights,

      vehicle_model: vehicle?.model || vehicle?.name || '',
      vehicle_price: vehicle ? parseFloat(vehicle.price) : 0,
      vehicle_days: vehicleDays,

      total_price: parseFloat(totalPrice),
      currency,
      is_package: isPackage
    };
  };

  const handleCheckout = async () => {
    setIsSubmitting(true);
    setError(null);

    try {
      if (!currentUser?.email) {
        throw new Error('Debes iniciar sesión para completar la reserva');
      }

      const urlBase = 'http://localhost:8000/trips/';
      const emailParam = `?user_email=${currentUser.email}`;

      const packageItems = cartItems.filter(item => item.isPackage);
      const individualItems = cartItems.filter(item => !item.isPackage);

      const payloads = [];

      // Procesar paquetes con total incluyendo impuestos (21%)
      for (const pkg of packageItems) {
        const subtotal = getSubtotal();
        const tax = getTax();
        const discount = getDiscount();
        const finalTotal = parseFloat((subtotal + tax - discount).toFixed(2));

        const payload = buildTripPayload({
          user: currentUser,
          flight: pkg.details.flight,
          hotel: pkg.details.hotel,
          vehicle: pkg.details.vehicle,
          isPackage: true,
          totalPrice: finalTotal,
          currency: pkg.currency || 'EUR',
          passengers: pkg.passengers || ''
        });
        payloads.push(payload);
      }

      // Procesar items individuales (vuelo + hotel + vehículo) con total incluyendo impuestos (21%)
      const flight = individualItems.find(i => i.type === 'flight');

      if (flight) {
        const hotel = individualItems.find(i => i.type === 'hotel') || {};
        const vehicle = individualItems.find(i => i.type === 'vehicle') || {};

        const subtotal = getSubtotal();
        const tax = getTax();
        const discount = getDiscount();
        const finalTotal = parseFloat((subtotal + tax - discount).toFixed(2));

        const payload = buildTripPayload({
          user: currentUser,
          flight,
          hotel,
          vehicle,
          isPackage: false,
          totalPrice: finalTotal,
          currency: flight.currency || hotel.currency || vehicle.currency || 'EUR',
          passengers: flight.passengers || ''
        });

        payloads.push(payload);
      }

      // Enviar todas las reservas al backend
      for (const tripPayload of payloads) {
        const response = await fetch(urlBase + emailParam, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(tripPayload),
        });

        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.detail || 'Error al crear la reserva');
        }
      }

      clearCart();
      setShowSuccessModal(true);
    } catch (err) {
      console.error('Error en checkout:', err);
      setError('Error al procesar la reserva: ' + err.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  const hasChildrenInCart = () => {
    return cartItems.some(item => {
      const passengers = item.passengers || '';
      const childMatch = passengers.match(/(\d+)\s*Niñ[oa]s?/i);
      return childMatch && parseInt(childMatch[1]) > 0;
    });
  };

  const getSubtotal = () => calculateTotal();

  const getTax = () => getSubtotal() * 0.21;

  const getDiscount = () => hasChildrenInCart() ? getSubtotal() * 0.2 : 0;

  const getFinalTotal = () => {
    const subtotal = getSubtotal();
    const tax = getTax();
    const discount = getDiscount();
    return (subtotal + tax - discount).toFixed(2);
  };


  return (
    <div className="cart-page" role="main" aria-labelledby="cart-title">
      <div className="cart-header">
        <h1 id="cart-title">Resumen de tu Reserva</h1>
        <div className="cart-stats">
          <span aria-live="polite">
            {cartItems.length} elemento{cartItems.length !== 1 && 's'}
          </span>
          <Link to="/" className="continue-shopping" aria-label="Volver a la página principal para seguir comprando">
            ← Seguir comprando
          </Link>
        </div>
      </div>

      <div className="cart-container">
        <div className="cart-items" aria-live="polite">
          {cartItems.length === 0 ? (
            <div className="empty-cart">
              <h2>Tu carrito está vacío</h2>
              <p>Comienza a agregar elementos desde nuestros servicios</p>
            </div>
          ) : (
            cartItems.map(item => (
              item.isPackage ? (
                <div 
                  key={item.id} 
                  className="cart-item package-item"
                  aria-label={`Paquete de viaje: ${item.name}`}
                >
                  <div className="item-icon" aria-hidden="true">
                    <FaBox />
                  </div>

                  <div className="item-details">
                    <div className="package-header">
                      <h3 className="item-title">{item.name}</h3>
                      <span className="package-badge">Paquete</span>
                    </div>

                    <div className="package-details">
                      <div className="package-component">
                        <span className="package-component-title">
                          <FaPlane aria-hidden="true" /> Vuelo:
                        </span>
                        <span>{item.details.flight.origin} → {item.details.flight.destination}</span>
                        <span>{item.passengers}</span>
                        <span className="package-component-price">
                          {item.details.flight.price} {item.currency}
                        </span>
                      </div>

                      <div className="package-component">
                        <span className="package-component-title">
                          <FaHotel aria-hidden="true" /> Hotel:
                        </span>
                        <span>{item.details.hotel.name} ({item.details.hotel.nights} noches)</span>
                        <span className="package-component-price">
                          {item.details.hotel.price} {item.currency}
                        </span>
                      </div>

                      {item.details.vehicle && (
                        <div className="package-component">
                          <span className="package-component-title">
                            <FaCar aria-hidden="true" /> Vehículo:
                          </span>
                          <span>{item.details.vehicle.model} ({item.details.vehicle.days} días)</span>
                          <span className="package-component-price">
                            {item.details.vehicle.price} {item.currency}
                          </span>
                        </div>
                      )}
                    </div>
                  </div>

                  <div className="item-price">
                    <span className="package-total">{item.price} {item.currency}</span>
                    <button 
                      className="remove-btn" 
                      onClick={() => removeFromCart(item.id)}
                      aria-label={`Eliminar paquete ${item.name} del carrito`}
                    >
                      <FaTrash aria-hidden="true" />
                    </button>
                  </div>
                </div>
              ) : (
                <div 
                  key={item.id} 
                  className="cart-item"
                  aria-label={`${item.type === 'flight' ? 'Vuelo' : item.type === 'hotel' ? 'Hotel' : 'Vehículo'}: ${item.name || `${item.origin} a ${item.destination}`}`}
                >
                  <div className="item-icon" aria-hidden="true">
                    {item.type === 'flight' && <FaPlane />}
                    {item.type === 'hotel' && <FaHotel />}
                    {item.type === 'vehicle' && <FaCar />}
                  </div>

                  <div className="item-details">
                    <h3 className="item-title">
                      {item.type === 'flight'
                        ? `${item.origin} → ${item.destination}`
                        : item.name}
                    </h3>

                    {item.type === 'flight' && (
                      <div className="flight-detailss">
                        <span>{item.departure}</span>
                        {item.returnDate && <span> - {item.returnDate}</span>} <br/>
                        <span>{item.passengers}</span>
                      </div>
                    )}

                    {item.type === 'hotel' && (
                      <div className="hotel-details">
                        <span>{item.nights} noches ({item.pricePerDay} {item.currency}/noche)</span>
                      </div>
                    )}

                    {item.type === 'vehicle' && (
                      <div className="vehicle-details">
                        <span>{item.days} días ({item.pricePerDay} {item.currency}/día)</span>
                        <span> Tipo: {item.vehicleType}</span>
                      </div>
                    )}
                  </div>

                  <div className="item-price">
                    <span>{Number(item.price).toFixed(2)} {item.currency}</span>
                    <button 
                      className="remove-btn" 
                      onClick={() => removeFromCart(item.id)}
                      aria-label={`Eliminar ${item.type === 'flight' ? 'vuelo' : item.type === 'hotel' ? 'hotel' : 'vehículo'} del carrito`}
                    >
                      <FaTrash aria-hidden="true" />
                    </button>
                  </div>
                </div>
              )
            ))
          )}
        </div>

        {cartItems.length > 0 && (
          <div 
            className="cart-summary"
            role="region"
            aria-labelledby="summary-title"
          >
            <h3 id="summary-title">Resumen del Pedido</h3>

            <div className="summary-row">
              <span>Subtotal:</span>
              <span>{getSubtotal().toFixed(2)} €</span>
            </div>

            {hasChildrenInCart() && (
              <div className="summary-row">
                <span>Descuento por niños (20%):</span>
                <span>- {getDiscount().toFixed(2)} €</span>
              </div>
            )}

            <div className="summary-row">
              <span>Impuestos (21%):</span>
              <span>{getTax().toFixed(2)} €</span>
            </div>

            <div className="summary-row total">
              <span><strong>Total:</strong></span>
              <span><strong>{getFinalTotal()} €</strong></span>
            </div>

            <button 
              className="checkout-btn" 
              onClick={handleCheckout}
              aria-label="Finalizar reserva y proceder al pago"
              disabled={isSubmitting}
            >
              Finalizar Reserva
              <span className="secure-checkout">
                <FaLock aria-hidden="true" /> Pago seguro
              </span>
            </button>

            {error && (
              <div className="error-message" role="alert">
                {error}
              </div>
            )}
          </div>
        )}

        {showSuccessModal && (
          <SuccessModal
            onClose={() => setShowSuccessModal(false)}
            onPay={() => navigate('/pago')}
          />
        )}

        {isSubmitting && (
          <LoadingSpinner 
            message="Procesando tu reserva, por favor espera..." 
            aria-live="polite"
          />
        )}
      </div>
    </div>
  );
}