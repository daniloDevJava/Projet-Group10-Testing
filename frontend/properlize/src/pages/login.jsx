
import { useState, useEffect } from 'react';
import '../style/sign.css';
import carBg from '../assets/car-bg.jpeg';
import logo from '../assets/logo.svg';

function Login() {
  const [currentSlide, setCurrentSlide] = useState(0);
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
  }, [slides.length]);

  return (
    <div className="body">
           <div className="container">
      <div className="left-section">
        <img src={carBg} alt="Car background" className="car-bg" />
        <div className="overlay"></div>
        <img src={logo} alt="Properlize" className="logo" />
        
        <div className="slider"> {/* Changé de slider-content à slider */}
          <h1 className="slide-text">{slides[currentSlide].split('\n').map((line, i) => (
            <div key={i}>{line}</div>
          ))}</h1>
          
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
        <h2>Login to your account</h2>
        <p>Don't have an account? <a href="sign">Sign in</a></p>
        
        <form className="auth-form">
          <input type="text" placeholder="Email or Login" />
          <input type="password" placeholder="Password" />
          <button type="submit">Login</button>
        </form>
      </div>
    </div>
    </div>
   
  );
}

export default Login;