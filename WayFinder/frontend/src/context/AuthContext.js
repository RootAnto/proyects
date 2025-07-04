import React, { createContext, useContext, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  // Verificar sesión al cargar
  useEffect(() => {
    const verifySession = async () => {
      try {
        const storedSession = localStorage.getItem('session');
        if (storedSession) {
          const { user, expiresAt } = JSON.parse(storedSession);

          if (Date.now() < expiresAt) {
            setCurrentUser(user);
          } else {
            localStorage.removeItem('session');
          }
        }
      } catch (error) {
        console.error('Error verifying session:', error);
        localStorage.removeItem('session');
      } finally {
        setLoading(false);
      }
    };

    verifySession();

    // Verificar expiración cada 30 segundos
    const interval = setInterval(verifySession, 30000);
    return () => clearInterval(interval);
  }, []);

  const login = async (email, password) => {
    try {
      setLoading(true);
      const response = await axios.post('http://localhost:8000/auth/login', { email, password });

      const sessionData = {
        user: response.data,
        expiresAt: Date.now() + 60 * 60 * 1000 // 1 hora
      };

      localStorage.setItem('session', JSON.stringify(sessionData));
      setCurrentUser(response.data);

      return response.data;
    } catch (error) {
      console.error('Login error:', error.response?.data);
      throw error;
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    setCurrentUser(null);
    localStorage.removeItem('session');
    navigate('/login');
  };

  const value = {
    currentUser,
    loading,
    login,
    logout,
    setCurrentUser
  };

  return (
    <AuthContext.Provider value={value}>
      {!loading && children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
