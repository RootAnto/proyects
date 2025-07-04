import React, { useState } from 'react';
import { auth, provider } from '../services/firebase';
import { signInWithPopup } from 'firebase/auth';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import axios from 'axios';
import '../styles/Login.css';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login, setCurrentUser } = useAuth();
  const navigate = useNavigate();

  const handleGoogleLogin = async () => {
    try {
      const result = await signInWithPopup(auth, provider);
      const user = result.user;

      const response = await axios.post('http://localhost:8000/auth/google-login', {
        email: user.email,
        nombre: user.displayName,
        uid: user.uid,
      });

      const sessionData = {
        user: {
          email: user.email,
          nombre: user.displayName
        },
        expiresAt: new Date().getTime() + (60 * 60 * 1000)
      };
      localStorage.setItem('session', JSON.stringify(sessionData));
      setCurrentUser(sessionData.user);

      console.log('Inicio de sesión con Google exitoso:', response.data);
      navigate('/');
    } catch (error) {
      console.error('Error al iniciar sesión con Google:', error);
      setError('Error al iniciar sesión con Google');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const user = await login(email, password);

      navigate('/');
    } catch (err) {
      setError(err.response?.data?.detail || 'Correo o contraseña incorrectos');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-content">
        <div className="login-card">
          <h1 className="login-title">Iniciar sesión</h1>
          {error && <div className="login-error">{error}</div>}
          <form onSubmit={handleSubmit} className="login-form">
            <div className="login-form-group">
              <label htmlFor="email" className="login-label">Correo electrónico</label>
              <input
                type="email"
                id="email"
                className="login-input"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="tucorreo@ejemplo.com"
                required
              />
            </div>
            <div className="login-form-group">
              <label htmlFor="password" className="login-label">Contraseña</label>
              <input
                type="password"
                id="password"
                className="login-input"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
                required
              />
            </div>
            <button type="submit" className="login-button" disabled={loading}>
              {loading ? 'Iniciando sesión...' : 'Iniciar sesión'}
            </button>
          </form>
          <div className="login-divider">
            <span className="login-divider-text">o continuar con</span>
          </div>
          <div className="login-social">
            <button type="button" className="login-social-button" onClick={handleGoogleLogin}>
              <img
                src="https://developers.google.com/identity/images/g-logo.png"
                alt="Google"
              />
              Continuar con Google
            </button>
          </div>
          <div className="login-footer">
            ¿No tienes una cuenta? <Link to="/register" className="login-link">Regístrate</Link>
          </div>
          {/* Botón Volver al inicio */}
          <button
            type="button"
            className="login-back-button"
            onClick={() => navigate('/')}
          >
            Volver al inicio
          </button>
        </div>
      </div>
    </div>
  );
};

export default Login;
