// App.jsx
import { useState, useEffect } from 'react';
import '../style/sign.css';
import carBg  from '../images/car-bg.jpeg'; 
import logo from '../images/logo.svg';

function Sign() {
  const [currentSlide, setCurrentSlide] = useState(0);
  const slides = [
    "Looking for a car?\nYou are there",
    "Find your dream car\nwith ease",
    "The best deals\njust for you"
  ];

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % slides.length);
    }, 3000);
    return () => clearInterval(interval);
  }, [slides.length]);

  return (
    <div className="container">
      <div className="left-section">
        <img src={carBg} alt="Car background" className="car-bg" />
        <div className="overlay"></div>
        <img src={logo} alt="Properlize" className="logo" />
        
        <div className="slider">
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
        <h2>Create an account</h2>
        <p>Already have an account? <a href="#">login</a></p>
        
        <form className="auth-form">
          <input type="text" placeholder="Login" />
          <input type="email" placeholder="Email" />
          <input type="password" placeholder="Password" />
          <button type="submit">Create an account</button>
        </form>
      </div>
    </div>
  );
}

export default Sign;