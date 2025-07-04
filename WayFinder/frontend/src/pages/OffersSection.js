import React from 'react';
import { FaChild, FaPlane, FaPercent } from 'react-icons/fa';
import '../styles/OffersSection.css';
import Header from '../components/Header';
import Footer from '../components/Footer';

const OffersSection = () => {
  return (
    <div className='flight-results-app'>
        <Header />
        <section className="offers-section">
        <h2 className="offers-title">Ofertas y Promociones</h2>
        
        <div className="offer-card">
            <div className="offer-icon">
            <FaChild size={40} />
            </div>
            <div className="offer-content">
            <h3>Descuento por viajar con niños</h3>
            <p>
                ¡Viaja en familia y ahorra! Obtén un <strong>20% de descuento</strong> en vuelos si incluyes al menos un niño en la reserva.
            </p>
            </div>
            <div className="offer-badge">
            <FaPercent /> -20%
            </div>
        </div>

        {/* Puedes añadir más ofertas aquí */}
        </section>
        <Footer />
    </div>
  );
};

export default OffersSection;
