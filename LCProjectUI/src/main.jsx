// src/main.jsx
import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter as Router } from 'react-router-dom'; // Import BrowserRouter
import App from './App';
import './index.css'; // Ensure this file exists and is correctly styled

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  <Router> {/* Wrap your App component with BrowserRouter */}
    <React.StrictMode>
      <App />
    </React.StrictMode>
  </Router>
);
