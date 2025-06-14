// Fichier : src/pages/HomePage.jsx (Aucune modification nécessaire)

import React from 'react';
import Hero from '../components/hero';
import CarList from '../components/CarList';
import Footer from '../components/Footer';

const HomePage = () => {
  return (
    <div className="homepage">
      <section className="hero-section">
        <Hero />
      </section>
      <section className="popular-section">
        <CarList />
      </section>
      <section className="Footer">
        <Footer />
      </section>
    </div>
  );
};

export default HomePage;