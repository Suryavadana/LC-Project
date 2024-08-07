import React from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from './axiosInstance'; // Import your configured Axios instance

const Logout = () => {
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await axiosInstance.get('/logout');
      localStorage.removeItem('token'); // Remove token if it was stored
      navigate('/login'); // Redirect to login page after logout
    } catch (error) {
      console.error('Logout failed', error);
    }
  };

  return (
    <button onClick={handleLogout} className="btn btn-primary">
      Logout
    </button>
  );
};

export default Logout;