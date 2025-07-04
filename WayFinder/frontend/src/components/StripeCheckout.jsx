import { useEffect, useState } from 'react';
import { useStripe, useElements, CardElement } from '@stripe/react-stripe-js';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import { useNavigate, useLocation } from 'react-router-dom';

function StripeCheckout() {
  const stripe = useStripe();
  const elements = useElements();
  const { currentUser } = useAuth();
  const { clearCart } = useCart();
  const navigate = useNavigate();
  const location = useLocation();

  const [clientSecret, setClientSecret] = useState(null);
  const [userEmail, setUserEmail] = useState(null);
  const [userName, setUserName] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState(null);

  const queryParams = new URLSearchParams(location.search);
  const tripId = queryParams.get('tripId');

  useEffect(() => {
    if (!tripId) {
      setError('ID del viaje no proporcionado en la URL.');
      return;
    }

    let isMounted = true;

    const fetchClientSecret = async () => {
      try {
        const res = await fetch(`http://localhost:8000/payments/payment-intent?trip_id=${tripId}`, {
          method: 'POST',
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.detail || 'Error al obtener client secret.');
        if (isMounted) setClientSecret(data.client_secret);
      } catch (err) {
        if (isMounted) setError(err.message);
      }
    };

    const fetchUserData = async () => {
      try {
        const res = await fetch(`http://localhost:8000/trips/get-email-from-trip?trip_id=${tripId}`);
        const data = await res.json();
        if (!res.ok) throw new Error(data.detail || 'Error al obtener datos del usuario.');
        if (isMounted) {
          setUserEmail(data.user_email);
          setUserName(data.user_name || 'Cliente');
        }
      } catch (err) {
        if (isMounted) setError(err.message);
      }
    };

    fetchClientSecret();
    fetchUserData();

    return () => {
      isMounted = false;
    };
  }, [tripId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccessMessage(null);

    const emailToUse = userEmail || currentUser?.email;

    if (!emailToUse) {
      setError('Debe proporcionarse un correo electrónico para completar el pago.');
      return;
    }

    if (!stripe || !elements) {
      setError('Stripe aún no está listo. Intenta más tarde.');
      return;
    }

    if (!clientSecret) {
      setError('No se pudo obtener el client secret. Recarga la página.');
      return;
    }

    setLoading(true);

    try {
      const cardElement = elements.getElement(CardElement);
      if (!cardElement) throw new Error('No se encontró el elemento de la tarjeta.');

      const { error: paymentError, paymentIntent } = await stripe.confirmCardPayment(clientSecret, {
        payment_method: {
          card: cardElement,
          billing_details: {
            name: userName || currentUser?.nombre || emailToUse,
            email: emailToUse,
          },
        },
      });

      if (paymentError) throw new Error(paymentError.message);

      if (paymentIntent.status === 'succeeded') {
        const confirmResponse = await fetch(
          `http://localhost:8000/trips/confirm-trip?trip_id=${tripId}&user_email=${encodeURIComponent(emailToUse)}`,
          {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
          }
        );

        const confirmData = await confirmResponse.json();

        if (!confirmResponse.ok) {
          throw new Error(confirmData.detail || 'Error al confirmar la reserva.');
        }

        const msg = confirmData.message || '';

        if (msg.includes('confirmada') && msg.includes('tickets')) {
          setSuccessMessage(msg);
          clearCart();
          setTimeout(() => navigate('/PaymentSuccess'), 3000);
        } else {
          setError(msg);
        }
      }
    } catch (err) {
      setError(err.message || 'Error procesando el pago.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="checkout-form-container">
      <h2>Pago con Tarjeta</h2>

      {error && (
        <div style={{ color: 'red', marginBottom: '1rem', textAlign: 'center' }}>
          <p>{error}</p>
        </div>
      )}

      {successMessage && (
        <p style={{ color: 'green', marginBottom: '1rem', textAlign: 'center' }}>{successMessage}</p>
      )}

      <form onSubmit={handleSubmit} className="checkout-form">
        <div className="form-group" style={{ marginBottom: '1rem' }}>
          <CardElement
            options={{
              style: {
                base: {
                  fontSize: '16px',
                  color: '#32325d',
                  '::placeholder': { color: '#a0aec0' },
                },
                invalid: { color: '#fa755a' },
              },
            }}
          />
        </div>

        <button
          type="submit"
          disabled={!stripe || loading}
          className="submit-payment-btn"
          style={{
            backgroundColor: loading ? '#94d3a2' : '#28a745',
            color: '#fff',
            padding: '12px 25px',
            border: 'none',
            borderRadius: '6px',
            cursor: loading ? 'not-allowed' : 'pointer',
            marginBottom: '1rem',
          }}
        >
          {loading ? 'Procesando...' : 'Pagar con Tarjeta'}
        </button>
      </form>

      <div style={{ textAlign: 'center' }}>
        <button
          onClick={() => navigate('/')}
          style={{
            padding: '10px 20px',
            backgroundColor: '#007bff',
            color: '#fff',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer',
          }}
        >
          Ir a Wayfinder
        </button>
      </div>
    </div>
  );
}

export default StripeCheckout;
