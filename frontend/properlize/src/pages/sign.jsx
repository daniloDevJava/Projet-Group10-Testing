// Fichier : src/pages/Sign.jsx (Version Corrigée et Complète)

import { useState, useEffect } from 'react';
// CORRECTION : Importer useNavigate pour gérer la redirection
import { Link, useNavigate } from 'react-router-dom';
import '../style/sign.css';
import carBg from '../assets/car-bg.jpeg';
import logo from '../assets/logo.svg';

function Sign() {
  // CORRECTION : Initialiser le hook useNavigate
  const navigate = useNavigate();
  const [currentSlide, setCurrentSlide] = useState(0);
  const [showPassword, setShowPassword] = useState(false);

  const slides = [
    "Looking for a car?\nYou are there",
    "Find your dream car\nwith ease",
    "The best deals\njust for you"
  ];

  const [formData, setFormData] = useState({
    name: '',
    email: '',
    mdp: ''
  });

  const handleChange = (e) => {
    setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const togglePassword = () => {
    setShowPassword(prev => !prev);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch('http://localhost:9000/users/add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });

      if (!response.ok) {
        // Si la réponse n'est pas OK (status 4xx ou 5xx), on lève une erreur.
        const errorData = await response.json().catch(() => ({ message: 'Erreur inconnue' }));
        throw new Error(errorData.message || 'Erreur lors de la création du compte');
      }

      // Si la réponse est OK
      await response.json(); // On consomme le corps de la réponse, même si on ne l'utilise pas
      alert('Compte créé avec succès !');

      // **LA CORRECTION LA PLUS IMPORTANTE**
      // On redirige l'utilisateur vers la page de connexion après le succès.
      navigate('/login');

    } catch (error) {
      console.error(error);
      alert(error.message || 'Échec de la création du compte');
    }
  };

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % slides.length);
    }, 3000);
    return () => clearInterval(interval);
  }, [slides.length]);

  return (
    <div className="body">
      <div className="container">
        <div className="left-section">
          <img src={carBg} alt="Car background" className="car-bg" />
          <div className="overlay"></div>
          <img src={logo} alt="Properlize" className="logo" />
          
          <div className="slider">
            <h1 className="slide-text">
              {slides[currentSlide].split('\n').map((line, i) => (
                <div key={i}>{line}</div>
              ))}
            </h1>
            <div className="slider-indicators">
              {slides.map((_, index) => (
                <div 
                  key={index} 
                  className={`indicator ${index === currentSlide ? 'active' : ''}`}
                />
              ))}
            </div>
          </div>
        </div>
        
        <div className="right-section">
          <h2>Create an account</h2>
          <p>Already have an account? <Link to="/login">Login</Link></p>
          
          <form className="auth-form" onSubmit={handleSubmit}>
            <input 
              type="text" 
              name="name" 
              placeholder="Login" 
              value={formData.name} 
              onChange={handleChange} 
              required
            />
            <input 
              type="email" 
              name="email" 
              placeholder="Email" 
              value={formData.email} 
              onChange={handleChange} 
              required
            />
            <div style={{ position: 'relative', width: '100%' }}>
              <input 
                type={showPassword ? 'text' : 'password'} 
                name="mdp" 
                placeholder="Password" 
                value={formData.mdp} 
                onChange={handleChange} 
                required
                style={{ width: '100%' }}
              />
              <span onClick={togglePassword} style={{ position: 'absolute', right: '10px', top: '50%', transform: 'translateY(-50%)', cursor: 'pointer', fontSize: '18px', color: '#555' }} title={showPassword ? "Cacher le mot de passe" : "Afficher le mot de passe"}>
                {showPassword ? '🙈' : '👁️'}
              </span>
            </div>
            <button type="submit">Create an account</button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default Sign;