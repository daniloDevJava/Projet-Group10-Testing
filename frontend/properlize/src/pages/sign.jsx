import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../style/sign.css';
import carBg from '../assets/car-bg.jpeg'; 
import logo from '../assets/logo.svg';

function Sign() {
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

  const navigate = useNavigate();

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
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
      });

      if (!response.ok) {
        throw new Error('Erreur lors de la création du compte');
      }

      const data = await response.json();
      alert('Compte créé avec succès !');
      console.log(data);
      navigate('/login');
    
    } catch (error) {
      console.error(error);
      alert('Échec de la création du compte');
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
              <span 
                onClick={togglePassword}
                style={{
                  position: 'absolute',
                  right: '10px',
                  top: '50%',
                  transform: 'translateY(-50%)',
                  cursor: 'pointer',
                  fontSize: '18px',
                  color: '#555'
                }}
                title={showPassword ? "Cacher le mot de passe" : "Afficher le mot de passe"}
              >
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
