import '../styles/Modal.css';

export default function SuccessModal({ onClose }) {
  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2>¡Reserva realizada con éxito!</h2>
        <p>Tu reserva ha sido registrada correctamente.</p>
        <p>Puedes confirmar la <b>reserva</b> desde tu <b>email</b> o desde <b>mis reservas</b> en la web de <b>WAYFINDER</b>.</p>
        <button
          className="btn-close"
          onClick={() => {
            onClose();
            window.location.href = '/';
          }}
        >
          Volver al inicio
        </button>
      </div>
    </div>
  );
}

