import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import logoImage from '../assests/LOGO.JPG'; // Asegúrate que la ruta sea correcta

const Header = () => {
  const { currentUser, logout } = useAuth();
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  useEffect(() => {
    const handleClickOutside = (e) => {
      if (isMenuOpen && !e.target.closest('.user-menu-container')) {
        setIsMenuOpen(false);
      }
    };

    document.addEventListener('click', handleClickOutside);
    return () => document.removeEventListener('click', handleClickOutside);
  }, [isMenuOpen]);

  return (
    <header className="header">
      <div className="container">
        {/* Logo con link a home */}
        <Link to="/" className="logo">
          <img
            src={logoImage}
            alt="WayFinder Logo"
            className="logo-image"
            style={{ height: '60px', objectFit: 'contain' }}
          />
        </Link>

        <nav className="nav">
          <Link to="/" className="nav-link">Inicio</Link>
          <Link to="/OffersSection" className="nav-link">Ofertas</Link>
          <Link to="/Contact" className="nav-link">Contacto</Link>
        </nav>

        <div className="auth-buttons">
          {currentUser ? (
            <div className="user-menu-container">
              <button
                className="user-menu-trigger"
                onClick={() => setIsMenuOpen(!isMenuOpen)}
              >
                <span className="user-avatar">
                  {currentUser.nombre.charAt(0).toUpperCase()}
                </span>
                <span className="user-welcome">{currentUser.nombre}</span>
                <span className={`dropdown-arrow ${isMenuOpen ? 'open' : ''}`}>▼</span>
              </button>

              {isMenuOpen && (
                <div className="user-dropdown">
                  <Link to="/profile" className="perfil" onClick={() => setIsMenuOpen(false)}>
                    Mi perfil
                  </Link>
                  <Link to="/my-bookings" className="perfil" onClick={() => setIsMenuOpen(false)}>
                    Mis reservas
                  </Link>
                  <button className="logout-btn" onClick={logout}>
                    Cerrar sesión
                  </button>
                </div>
              )}
            </div>
          ) : (
            <>
              <Link to="/login" className="login-btn">Iniciar sesión</Link>
              <Link to="/register" className="register-btn">Registrarse</Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;
