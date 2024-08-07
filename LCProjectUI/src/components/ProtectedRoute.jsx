import React from 'react';
import { Route, Navigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext'; // Import your AuthContext

const ProtectedRoute = ({ component: Component, ...rest }) => {
  const { user } = useAuth(); // Get user data from context

  return (
    <Route
      {...rest}
      element={user ? <Component /> : <Navigate to="/login" />}
    />
  );
};

export default ProtectedRoute;