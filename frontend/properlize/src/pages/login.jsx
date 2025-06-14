import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../style/sign.css';
import carBg from '../assets/car-bg.jpeg';
import logo from '../assets/logo.svg';

function Login() {
  const [currentSlide, setCurrentSlide] = useState(0);
  const [showPassword, setShowPassword] = useState(false);
  const [formData, setFormData] = useState({
    email: '',
    login: '',
    password: ''
  });

  const navigate = useNavigate();

  const slides = [
    "Welcome back",
    "Looking for a car?\nYou are there",
    "Find your dream car\nwith ease"
  ];

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % slides.length);
    }, 3000);
    return () => clearInterval(interval);
  }, []);

  const handleChange = (e) => {
    setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const togglePassword = () => {
    setShowPassword(prev => !prev);
  };
  const handleSubmit = async (e) => {
    e.preventDefault();
  
    try {
      const response = await fetch('http://localhost:9000/users/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          email: formData.email,
          login: formData.login,
          mdp: formData.password
        })
      });
  
      if (!response.ok) {
        throw new Error('Échec de la connexion');
      }
  
      const data = await response.json();
  
    
      localStorage.setItem("userLogin", formData.login);
  
      alert('Connexion réussie !');
      navigate('/home');
  
    } catch (error) {
      console.error(error);
      alert('Email, login ou mot de passe incorrect');
    }
  };
  

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
                <div key={index} className={`indicator ${index === currentSlide ? 'active' : ''}`} />
              ))}
            </div>
          </div>
        </div>

        <div className="right-section">
          <h2>Login to your account</h2>
          <p>Don't have an account? <Link to="/sign">Sign up</Link></p>

          <form className="auth-form" onSubmit={handleSubmit}>
          <input 
              type="text"
              name="login"
              placeholder="Login"
              value={formData.login}
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

            <div className="password-field">
              <input 
                type={showPassword ? 'text' : 'password'}
                name="password"
                placeholder="Password"
                value={formData.password}
                onChange={handleChange}
                required
              />
              <span 
                onClick={togglePassword}
                className="toggle-password"
                title={showPassword ? "Cacher le mot de passe" : "Afficher le mot de passe"}
              >
                {showPassword ? '🙈' : '👁️'}
              </span>
            </div>

            <button type="submit">Login</button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default Login;
