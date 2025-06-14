import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaSearch, FaSignOutAlt, FaTimes } from 'react-icons/fa';
import noImage from '../assets/car1.jpg'; // mets un vrai fichier image fallback ici

function Home() {
  const [cars, setCars] = useState([]);
  const [likedCars, setLikedCars] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [searchType, setSearchType] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [error, setError] = useState(null);

  const navigate = useNavigate();
  const userLogin = localStorage.getItem("userLogin");

  useEffect(() => {
    const fetchAll = async () => {
      try {
        const res = await fetch('http://localhost:9000/vehicule/all');
        const data = await res.json();
        setCars(data);
      } catch (err) {
        console.error("Erreur de chargement des véhicules", err);
      }
    };
    fetchAll();
  }, []);

  const toggleLike = (id) => {
    setLikedCars((prev) =>
      prev.includes(id) ? prev.filter((v) => v !== id) : [...prev, id]
    );
  };

  const handleSearch = async () => {
    if (!searchTerm || !searchType) return;

    let url = '';
    if (searchType === 'id') {
      url = `http://localhost:9000/vehicule/id/${searchTerm}`;
    } else if (searchType === 'number') {
      url = `http://localhost:9000/vehicule/number/${searchTerm}`;
    } else if (searchType === 'price') {
      url = `http://localhost:9000/vehicule/all`;
    }

    try {
      const res = await fetch(url);
      if (!res.ok) throw new Error('Erreur lors de la recherche');
      const data = await res.json();

      let results = [];
      if (searchType === 'price') {
        results = data.filter(v => v.rentalPrice === parseInt(searchTerm));
      } else {
        results = Array.isArray(data) ? data : [data];
      }

      setSearchResults(results);
      setError(null);
    } catch (err) {
      setError('Aucun véhicule trouvé ou erreur de recherche');
      setSearchResults([]);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("userLogin");
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    navigate('/login');
  };

  const renderCarCard = (car) => {
    const isLiked = likedCars.includes(car.id || car.registrationNumber);
    const imageUrl =
      car.cheminVersImage && car.cheminVersImage.trim() !== ""
        ? `http://localhost:9000/${car.cheminVersImage}`
        : noImage;

    return (
      <div className="car-card" key={car.id || car.registrationNumber}>
        <div className="car-image-container">
          <img src={car.cheminVersImage} alt={car.model || "Car"} className="car-image" />
          <span
            className={`heart ${isLiked ? "liked" : ""}`}
            onClick={() => toggleLike(car.id || car.registrationNumber)}
          >
            {isLiked ? "♥" : "♡"}
          </span>
        </div>
        <div className="car-info">
          <h3>{car.make || "Inconnu"} {car.model || ""}</h3>
          <p className="annee-vehicule">Année: {car.year || 'N/A'}</p>
          <p className="price">{car.rentalPrice ? `${car.rentalPrice} FCFA` : "Prix non disponible"}</p>
          <button className="view-btn">Voir Détails</button>
        </div>
      </div>
    );
  };

  return (
    <div className="conteneur-principal">
      <div className="entete">
        <div className="texte-bienvenue">
          <h1>Bienvenue {userLogin}</h1>
        </div>
        <button className ='logout-btn' onClick={handleLogout}>
          <FaSignOutAlt /> Logout
        </button>
      </div>

      <div className="zone-recherche">
        <input
          type="text"
          placeholder="Rechercher un véhicule"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <select  onChange={(e) => setSearchType(e.target.value)} defaultValue="" >
          <option value="" disabled>-- Choisir un critère --</option>
          <option value="id">Par ID</option>
          <option value="number">Par Numéro</option>
          <option value="price">Par Prix</option>
        </select>
        <FaSearch className="icone-recherche" onClick={handleSearch} style={{ cursor: 'pointer' }} />
      </div>

      {error && <p style={{ color: 'red' }}>{error}</p>}

      {searchResults.length > 0 && (
        <div style={{ marginTop: 20 }}>
          <div style={{ display: 'flex', justifyContent: 'space-between' }}>
            <h2>Résultats de la recherche</h2>
            <FaTimes onClick={() => setSearchResults([])} style={{ cursor: 'pointer', fontSize: '18px' }} />
          </div>
          <div className="car-grid">
            {searchResults.map(renderCarCard)}
          </div>
        </div>
      )}

      <h2 className="titre-liste">Tous les véhicules disponibles</h2>
      <div className="car-grid">
        {cars.map(renderCarCard)}
      </div>
    </div>
  );
}

export default Home;