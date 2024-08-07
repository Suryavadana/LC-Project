// src/components/RegistrationForm.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/RegistrationForm.css';

const RegistrationForm = () => {
  const [form, setForm] = useState({
    username: '',
    password: '',
    verifyPassword: '',
  });
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const { password, verifyPassword } = form;

    if (password !== verifyPassword) {
      setMessage('Passwords do not match');
      return;
    }

    try {
      const response = await axios.post('http://localhost:8080/auth/register', form);
      setMessage(response.data.message);

      if (response.status === 201) {
        navigate('/login'); // Redirect to login page after successful registration
      }
    } catch (error) {
      if (error.response) {
        setMessage(error.response.data.message || 'Registration failed');
      } else {
        setMessage('An error occurred. Please try again.');
      }
    }
  };

  return (
    <div className="container mt-5">
      <div className="card">
        <div className="card-body">
          <div className="mb-4">
            <button className="btn btn-secondary me-2" onClick={() => navigate('/login')}>Login</button>
            <button className="btn btn-secondary me-2" onClick={() => navigate(-1)}>Back</button>
            <button className="btn btn-secondary me-2" onClick={() => navigate('/about')}>About</button>
            <button className="btn btn-secondary" onClick={() => navigate('/contact')}>Contact</button>
          </div>
          <h2 className="card-title">Registration Form</h2>
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label className="form-label">Username:</label>
              <input
                type="text"
                className="form-control"
                name="username"
                value={form.username}
                onChange={handleChange}
                required
                autoComplete="username"
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Password:</label>
              <input
                type="password"
                className="form-control"
                name="password"
                value={form.password}
                onChange={handleChange}
                required
                autoComplete="new-password"
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Verify Password:</label>
              <input
                type="password"
                className="form-control"
                name="verifyPassword"
                value={form.verifyPassword}
                onChange={handleChange}
                required
                autoComplete="new-password"
              />
            </div>
            <button type="submit" className="btn btn-primary">Register</button>
          </form>
          {message && <p className="mt-3 text-danger">{message}</p>}
        </div>
      </div>
    </div>
  );
};

export default RegistrationForm;
