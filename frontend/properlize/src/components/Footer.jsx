import React from "react";
import "../style/Footer.css";

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-links">
        <a href="#">Accueil</a>
        <a href="#">À propos</a>
        <a href="#">Voitures</a>
        <a href="#">Contact</a>
      </div>
      <div className="footer-bottom">
        <p>&copy; {new Date().getFullYear()} Properlize Groupe10. Tous droits réservés.</p>
      </div>
    </footer>
  );
};

export default Footer;
