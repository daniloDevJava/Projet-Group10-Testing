
import React from 'react';
//import '../style/HomeUser.css';
import { FiBell } from 'react-icons/fi';

const categories = ['All', 'Mercedes', 'Toyota', 'Landrover', 'Mercedes', 'Mercedes', 'Mercedes'];
const cars = new Array(8).fill({
  name: 'BMW 207',
  description: 'Compare the type checking and the scope',
  price: '8000 FCFA',
  image: 'https://source.unsplash.com/400x200/?car',
});

export default function Home() {
  return (
    <div className="homepage">
      {/* Header */}
      <header className="home-header">
        <h1 className="app-name">Properlize</h1>
        <div className="header-actions">
          <FiBell className="notif-icon" />
          <button className="logout-btn">Logout</button>
        </div>
      </header>

      {/* Greeting */}
      <section className="welcome">
        <h2>Hello Winnie</h2>
        <p>Welcome back</p>
      </section>

      {/* Title */}
      <section className="page-title">
        <h3>Pick the perfect vehicle<br />for every occasion</h3>
        <input className="search-input" placeholder="Enter keyword" />
      </section>

      {/* Categories */}
      <section className="category-section">
        <h4>Categories</h4>
        <div className="category-buttons">
          {categories.map((cat, index) => (
            <button key={index} className="category-btn">{cat}</button>
          ))}
        </div>
      </section>

      {/* Cars (Top Section) */}
      <section className="car-section">
        <div className="car-grid">
          {cars.slice(0, 3).map((car, idx) => (
            <div key={idx} className={`car-card ${idx === 2 ? 'highlighted' : ''}`}>
              <img src={car.image} alt={car.name} />
              <div className="heart">♡</div>
              <h5>{car.name}</h5>
              <p>{car.description}</p>
              <p className="price">{car.price}</p>
              <button className="view-btn">View Details</button>
            </div>
          ))}
        </div>
      </section>

      {/* Most popular */}
      <section className="popular-section">
        <h4>Most popular</h4>
        <div className="car-grid">
          {cars.map((car, idx) => (
            <div key={idx} className="car-card">
              <img src={car.image} alt={car.name} />
              <div className="heart">♡</div>
              <h5>{car.name}</h5>
              <p>{car.description}</p>
              <p className="price">{car.price}</p>
              <button className="view-btn">View Details</button>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}
