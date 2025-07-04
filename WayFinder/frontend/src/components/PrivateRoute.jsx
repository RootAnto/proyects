import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const PrivateRoute = ({ children }) => {
  const { currentUser, loading } = useAuth();

  if (loading) {
    return <div>Cargando sesión...</div>;
  }

  return currentUser ? children : <Navigate to="/login" replace />;
};

export default PrivateRoute;
