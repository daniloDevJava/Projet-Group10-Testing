import React, { useEffect, useState } from "react";
// AJOUT : Importation du composant Link
import { Link } from "react-router-dom";
import "../style/Hero.css";
import logo from "../assets/logo.svg";
import car1 from "../assets/car4.jpg";
import car2 from "../assets/car-bg.jpeg";
import car3 from "../assets/car3.jpg";

const carouselItems = [
  { text: "Properlize - Your road, our mission.", image: car2 },
  { text: "Drive more worryless. Rent the best car for your journey Online.", image: car1 },
  { text: "Explore freedom. Book your next ride with confidence.", image: car3 },
];

const Hero = () => {
  const [index, setIndex] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setIndex((prev) => (prev + 1) % carouselItems.length);
    }, 5000);
    return () => clearInterval(interval);
  }, []);

  const current = carouselItems[index];

  return (
    <div className="hero" style={{ backgroundImage: `url(${current.image})` }}>
      <div className="navbar">
        <Link to="/">
          <img src={logo} alt="Properlize Logo" width={150} />
        </Link>
        <div className="nav-links">
          <a href="#">All</a>
          <a href="#">Toyota</a>
          <a href="#">Mercedes</a>
          <a href="#">Landcruiser</a>
        </div>
        <div className="connexion-btn">
          {/* CORRECTION : Remplacement de <a> par <Link> pour une navigation SPA */}
          <Link to="/sign">
            <button className="sign-btn">
              <p>Signin</p>
            </button>
          </Link>
         
          {/* CORRECTION : Remplacement de <a> par <Link> */}
          <Link to="/login">
            <button className="login-btn">
              <p>Login</p>
            </button>
          </Link>
        </div>
      </div>

      <div className="hero-content">
        <p className="tagline">Drive swift, arrive happy</p>
        <h1>{current.text}</h1>
        <button className="cta-btn">Find a car now</button>
      </div>
    </div>
  );
};

export default Hero;