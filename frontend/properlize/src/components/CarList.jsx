import React, { useState } from "react";
import "../style/CarList.css"; 
import car1 from "../assets/car4.jpg";
import car2 from "../assets/car-bg.jpeg";
import car3 from "../assets/car3.jpg";
import car4 from "../assets/car5.jpg";
import car5 from "../assets/car6.jpg";
import car6 from "../assets/car7.jpg";
import car7 from "../assets/car8.jpg";

const cars = [
  { id: 1, name: "BMW 207", description: "Compare the type checking and the scope", price: "8000 FCFA", image: car1 },
  { id: 2, name: "BMW 207", description: "Compare the type checking and the scope", price: "8000 FCFA", image: car2 },
  { id: 3, name: "BMW 207", description: "Compare the type checking and the scope", price: "8000 FCFA", image: car3 },
  { id: 4, name: "BMW 207", description: "Compare the type checking and the scope", price: "8000 FCFA", image: car4 },
  { id: 5, name: "BMW 207", description: "Compare the type checking and the scope", price: "8000 FCFA", image: car5 },
  { id: 6, name: "BMW 207", description: "Compare the type checking and the scope", price: "8000 FCFA", image: car6 },
  { id: 7, name: "BMW 207", description: "Compare the type checking and the scope", price: "8000 FCFA", image: car7 },
  { id: 1, name: "BMW 207", description: "Compare the type checking and the scope", price: "8000 FCFA", image: car1 },
  { id: 2, name: "BMW 207", description: "Compare the type checking and the scope", price: "8000 FCFA", image: car2 },
  { id: 3, name: "BMW 207", description: "Compare the type checking and the scope", price: "8000 FCFA", image: car3 },
  { id: 4, name: "BMW 207", description: "Compare the type checking and the scope", price: "8000 FCFA", image: car4 },
  { id: 5, name: "BMW 207", description: "Compare the type checking and the scope", price: "8000 FCFA", image: car5 },
];

const CarList = () => {
  const [likedCars, setLikedCars] = useState([]);

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
          const isLiked = likedCars.includes(car.id);
          return (
            <div className="car-card" key={car.id}>
              <div className="car-image-container">
                <img src={car.image} alt={car.name} className="car-image" />
                <span
                    className={`heart ${isLiked ? "liked" : ""}`}
                  onClick={() => toggleLike(car.id)}
                >
                  {isLiked ? "♥" : "♡"}
                </span>

              </div>
              <div className="car-info">
                <h3>{car.name}</h3>
                <p className="desc">{car.description}</p>
                <p className="price">{car.price}</p>
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
