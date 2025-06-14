import React, { useState, useEffect } from "react";
import "../style/CarList.css";
import axios from "axios";
import noImage from "../assets/car1.jpg"; // Image par défaut

const CarList = () => {
  const [cars, setCars] = useState([]);
  const [likedCars, setLikedCars] = useState([]);

  // Récupération des véhicules depuis l'API
  useEffect(() => {
    const fetchCars = async () => {
      try {
        const response = await axios.get("http://localhost:9000/vehicule/all");
        setCars(response.data);
      } catch (error) {
        console.error("Erreur lors de la récupération des véhicules :", error);
      }
    };

    fetchCars();
  }, []);

  const toggleLike = (carId) => {
    setLikedCars((prevLikedCars) =>
      prevLikedCars.includes(carId)
        ? prevLikedCars.filter((id) => id !== carId)
        : [...prevLikedCars, carId]
    );
  };

  return (
    <div className="popular-container">
      <h2 className="popular-title">Most popular</h2>
      <div className="car-grid">
        {cars.map((car) => {
          const isLiked = likedCars.includes(car.id || car.registrationNumber); // clé unique
          const imageUrl =
            car.cheminVersImage && car.cheminVersImage.trim() !== ""
              ? car.cheminVersImage
              : noImage;

          return (
            <div className="car-card" key={car.id || car.registrationNumber}>
              <div className="car-image-container">
                <img src={imageUrl} alt={car.model || "Car"} className="car-image" />
                <span
                  className={`heart ${isLiked ? "liked" : ""}`}
                  onClick={() => toggleLike(car.id || car.registrationNumber)}
                >
                  {isLiked ? "♥" : "♡"}
                </span>
              </div>
              <div className="car-info">
                <h3>{car.make || "Unknown"} {car.make || ""}</h3>
            
                <p className="annee-vehicule">Year: {car.year || 'N/A'}</p>

                <p className="price">{car.rentalPrice ? `${car.rentalPrice} FCFA` : "Price not available"}</p>
                <button className="view-btn">View Details</button>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default CarList;
