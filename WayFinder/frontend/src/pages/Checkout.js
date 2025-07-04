import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import '../styles/Cart.css';
import { FaTrash, FaPlane, FaHotel, FaCar, FaLock } from 'react-icons/fa';

export default function CartPage() {
  const { cartItems, removeFromCart, clearCart } = useCart();
  const { currentUser } = useAuth();
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const subtotal = cartItems.reduce((sum, item) => sum + Number(item.price), 0);
  const tax = subtotal * 0.21;
  const total = subtotal + tax;

  const buildTripPayload = ({
    user,
    flight,
    hotel,
    vehicle,
    isPackage = false,
    totalPrice = 0,
    currency = 'EUR',
  }) => {
    const departure = new Date(flight.departure);
    const returnDate = flight.returnDate ? new Date(flight.returnDate) : null;
    const hotelNights = hotel.nights || (returnDate ? Math.ceil((returnDate - departure) / (1000 * 60 * 60 * 24)) : 1);
    const vehicleDays = vehicle?.days || hotelNights;

    return {
      user_id: String(user.id),
      origin: flight.origin,
      destination: flight.destination,
      departure_date: flight.departure,
      return_date: flight.returnDate || null,
      adults: 1,
      children: 0,
      user_email: user.email || '',
      user_name: user.nombre || '',

      flight_id: String(flight.id || ''),
      hotel_id: String(hotel.id || hotel.hotelId || ''),
      vehicle_id: vehicle ? String(vehicle.id || vehicle.vehicleId) : '',

      flight_price: parseFloat(flight.price),
      flight_name: flight.airline || '',

      hotel_name: hotel.name || '',
      hotel_price: parseFloat(hotel.price),
      hotel_nights: hotelNights,

      vehicle_model: vehicle?.model || vehicle?.name || vehicle?.brand || '',
      vehicle_price: vehicle ? parseFloat(vehicle.price) : 0,
      vehicle_days: vehicleDays,

      total_price: parseFloat(totalPrice),
      currency: currency,
      is_package: isPackage
    };
  };

  const handleCheckout = async () => {
    setIsSubmitting(true);
    setError(null);

    try {
      const urlBase = 'http://localhost:8000/trips/';
      const emailParam = `?user_email=${currentUser.email}`;

      const packageItems = cartItems.filter(item => item.isPackage);
      const individualItems = cartItems.filter(item => !item.isPackage);

      const payloads = [];

      for (const pkg of packageItems) {
        const payload = buildTripPayload({
          user: currentUser,
          flight: pkg.details.flight,
          hotel: pkg.details.hotel,
          vehicle: pkg.details.vehicle,
          isPackage: true,
          totalPrice: pkg.price,
          currency: pkg.currency || 'EUR'
        });
        payloads.push(payload);
      }

      const flight = individualItems.find(i => i.type === 'flight');
      const hotel = individualItems.find(i => i.type === 'hotel');

      if (flight && hotel) {
        const vehicle = individualItems.find(i => i.type === 'vehicle');
        const totalPrice =
          parseFloat(flight.price) +
          parseFloat(hotel.price) +
          (vehicle ? parseFloat(vehicle.price) : 0);

        const payload = buildTripPayload({
          user: currentUser,
          flight,
          hotel,
          vehicle,
          isPackage: false,
          totalPrice,
          currency: flight.currency || hotel.currency || vehicle?.currency || 'EUR'
        });

        payloads.push(payload);
      }

      for (const tripPayload of payloads) {
        const response = await fetch(urlBase + emailParam, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(tripPayload),
        });

        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.detail || 'Error al crear el viaje');
        }

        await response.json();
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

  return (
    <div className="cart-page">
      <div className="cart-header">
        <h1>Resumen de tu Reserva</h1>
        <div className="cart-stats">
          <span>{cartItems.length} elemento{cartItems.length !== 1 && 's'}</span>
          <Link to="/" className="continue-shopping">← Seguir comprando</Link>
        </div>
      </div>

      <div className="cart-container">
        <div className="cart-items">
          {cartItems.length === 0 ? (
            <div className="empty-cart">
              <h2>Tu carrito está vacío</h2>
              <p>Agrega servicios para reservar.</p>
            </div>
          ) : (
            cartItems.map(item => (
              <div key={item.id} className="cart-item">
                <div className="item-icon">
                  {item.type === 'flight' && <FaPlane />}
                  {item.type === 'hotel' && <FaHotel />}
                  {item.type === 'vehicle' && <FaCar />}
                </div>
                <div className="item-details">
                  <h3>
                    {item.type === 'flight'
                      ? `${item.origin} → ${item.destination}`
                      : item.name}
                  </h3>
                  {item.type === 'flight' && (
                    <p>{item.departure}{item.returnDate && ` - ${item.returnDate}`}</p>
                  )}
                  {item.type === 'hotel' && (
                    <p>{item.nights} noches ({item.pricePerDay} {item.currency}/noche)</p>
                  )}
                  {item.type === 'vehicle' && (
                    <p>{item.days} días ({item.pricePerDay} {item.currency}/día)</p>
                  )}
                </div>
                <div className="item-price">
                  <span>{item.price} {item.currency}</span>
                  <button
                    className="remove-btn"
                    onClick={() => removeFromCart(item.id)}
                  >
                    <FaTrash />
                  </button>
                </div>
              </div>
            ))
          )}
        </div>

        {cartItems.length > 0 && (
          <div className="cart-summary">
            <h3>Resumen del Pedido</h3>
            <div className="summary-row"><span>Subtotal:</span><span>{subtotal.toFixed(2)} €</span></div>
            <div className="summary-row"><span>Impuestos (21%):</span><span>{tax.toFixed(2)} €</span></div>
            <div className="summary-row total"><span>Total:</span><span>{total.toFixed(2)} €</span></div>
            <button
              onClick={handleCheckout}
              className="checkout-btn"
              disabled={isSubmitting}
            >
              {isSubmitting ? 'Procesando...' : 'Reservar'}
              <span className="secure-checkout"><FaLock /> Pago seguro</span>
            </button>
            {error && <p className="error-message">{error}</p>}
          </div>
        )}
      </div>

      {showSuccessModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2>¡Reserva realizada con éxito!</h2>
            <p>Te hemos enviado un correo de confirmación a <strong>{currentUser.email}</strong>.</p>
            <button
              className="btn-close"
              onClick={() => { setShowSuccessModal(false); navigate('/'); }}
            >
              Volver al inicio
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
