// LoadingSpinner.jsx
import React from 'react';
import '../styles/LoadingSpinner.css';

const LoadingSpinner = ({ message = "Cargando..." }) => (
  <div className="loading-overlay">
    <div className="spinner" />
    <p>{message}</p>
  </div>
);

export default LoadingSpinner;
