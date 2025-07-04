import React, { useState } from 'react';
import '../styles/Contact.css';
import Header from '../components/Header';
import Footer from '../components/Footer';

const Contact = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');

  const handleSend = () => {
    if (name.trim() === '' || email.trim() === '' || message.trim() === '') {
      alert('Por favor, rellena todos los campos antes de enviar.');
      return;
    }
    alert('¡Gracias! Tu mensaje ha sido enviado.');
    setName('');
    setEmail('');
    setMessage('');
  };

  return (
    <div className='flight-results-app'>
      <Header />

      <section className="contacto-container">
        <div className="contacto-formulario">
            <h2 className="contacto-titulo">Contáctanos</h2>

            <div className="form-grupo">
                <label htmlFor="name">Nombre</label>
                <input
                type="text"
                id="name"
                placeholder="Tu nombre"
                value={name}
                onChange={(e) => setName(e.target.value)}
                />
            </div>

            <div className="form-grupo">
                <label htmlFor="email">Correo electrónico</label>
                <input
                type="email"
                id="email"
                placeholder="tu@email.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                />
            </div>

            <div className="form-grupo">
                <label htmlFor="message">Mensaje</label>
                <textarea
                id="message"
                rows="4"
                placeholder="Escribe tu mensaje aquí..."
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                ></textarea>
            </div>

            <button type="button" className="btn-enviar" onClick={handleSend}>
                Enviar mensaje
            </button>
        </div>
      </section>

      <Footer />
    </div>
  );
};

export default Contact;
