import { createContext, useContext, useState, useEffect } from 'react';
import { useAuth } from './AuthContext';
import '../styles/Checkout.css';  

const CartContext = createContext();

export function CartProvider({ children }) {
  const { currentUser } = useAuth();
  const [cartItems, setCartItems] = useState([]);

  // Cargar/limpiar carrito al cambiar de usuario
 useEffect(() => {
    if (currentUser) {
      const key = `cart_${currentUser.id}`;
      const savedCart = localStorage.getItem(key);
      setCartItems(savedCart ? JSON.parse(savedCart) : []);
    } else {
      setCartItems([]); // Limpiar si no hay usuario
    }
  }, [currentUser]); 

  // Guardar en localStorage
  useEffect(() => {
    if (currentUser) {
      const key = `cart_${currentUser.id}`;
      localStorage.setItem(key, JSON.stringify(cartItems));
    }
  }, [cartItems, currentUser]);

  const addToCart = (item) => {
    setCartItems(prev => [...prev, { ...item, id: Date.now() }]);
  };

  const removeFromCart = (itemId) => {
    setCartItems(prev => prev.filter(item => item.id !== itemId));
  };

  // Limpiar el carrito al cerrar sesión
  const clearCart = () => {
    setCartItems([]);
    if (currentUser) {
      const key = `cart_${currentUser.id}`;
      localStorage.removeItem(key);
    }
  };

  return (
    <CartContext.Provider value={{ cartItems, addToCart, removeFromCart, clearCart }}>
      {children}
    </CartContext.Provider>
  );
}

export const useCart = () => useContext(CartContext);