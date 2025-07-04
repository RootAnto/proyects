import { Link } from 'react-router-dom';
import { FaCheckCircle } from 'react-icons/fa';
import '../styles/PaymentSuccess.css';

function PaymentSuccess() {
  return (
    <div className="success-container">
      <FaCheckCircle className="success-icon" />
      <h1>¡Pago realizado con éxito!</h1>
      <p>Tu reserva ha sido procesada correctamente. Recibirás un correo con los detalles y confirmar el pago.</p>
      <Link to="/" className="success-button">
        Volver al inicio
      </Link>
    </div>
  );
}

export default PaymentSuccess;
