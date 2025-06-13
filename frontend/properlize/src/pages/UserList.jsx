import React, { useState, useEffect } from 'react';
import '../style/UserListUI.css'; 
import Notification from '../assets/Notification.svg';
import logo from '../assets/logo.svg';
import avatar from "../assets/placeholder.svg";
import { FaUser, FaSignOutAlt, FaSearch, FaBell, FaPen, FaTrash } from "react-icons/fa";
import axios from 'axios';

function App() {
  const [utilisateurs, setUtilisateurs] = useState([]);
  const [utilisateursSelectionnes, setUtilisateursSelectionnes] = useState([]);
  const [toutSelectionner, setToutSelectionner] = useState(false);
  const [afficherFormulaireAdd, setAfficherFormulaireAdd] = useState(false);
  const [afficherFormulaireEdit, setAfficherFormulaireEdit] = useState(false);
  const [formulaireAdd, setFormulaireAdd] = useState({ name: '', email: '', password: '' });
  const [formulaireEdit, setFormulaireEdit] = useState({ name: '', email: '', password: '' });
  const [indexEdition, setIndexEdition] = useState(null);
  const [recherche, setRecherche] = useState('');
  const [utilisateurRecherche, setUtilisateurRecherche] = useState(null);

  useEffect(() => {
    chargerUtilisateurs();
  }, []);

  const chargerUtilisateurs = async () => {
    try {
      const response = await axios.get('http://localhost:9000/users/all');
      setUtilisateurs(response.data);
      setUtilisateurRecherche(null);
    } catch (error) {
      console.error("Erreur lors du chargement des utilisateurs :", error);
    }
  };

  const basculerSelection = () => {
    if (toutSelectionner) {
      setUtilisateursSelectionnes([]);
    } else {
      setUtilisateursSelectionnes(utilisateurs.map((_, index) => index));
    }
    setToutSelectionner(!toutSelectionner);
  };

  const selectionnerUtilisateur = (index) => {
    if (utilisateursSelectionnes.includes(index)) {
      setUtilisateursSelectionnes(utilisateursSelectionnes.filter((i) => i !== index));
    } else {
      setUtilisateursSelectionnes([...utilisateursSelectionnes, index]);
    }
  };

  const supprimerUtilisateur = async (index) => {
    const utilisateur = utilisateurs[index];
    try {
      await axios.delete(`http://localhost:9000/users/${utilisateur.id}`);
      const misAJour = utilisateurs.filter((_, i) => i !== index);
      setUtilisateurs(misAJour);
      setUtilisateursSelectionnes(utilisateursSelectionnes.filter((i) => i !== index));
    } catch (error) {
      console.error("Erreur lors de la suppression :", error);
    }
  };

  const supprimerUtilisateursSelectionnes = async () => {
    for (const index of utilisateursSelectionnes) {
      await supprimerUtilisateur(index);
    }
    setToutSelectionner(false);
  };

  const clicAjouterUtilisateur = () => {
    setFormulaireAdd({ name: '', email: '', password: '' });
    setAfficherFormulaireAdd(true);
    setAfficherFormulaireEdit(false);
  };

  const modifierUtilisateur = (index) => {
    const user = utilisateurs[index];
    setFormulaireEdit({ name: user.name, email: user.email, password: '' });
    setIndexEdition(index);
    setAfficherFormulaireEdit(true);
    setAfficherFormulaireAdd(false);
  };

  const gererChangementAdd = (e) => {
    const { name, value } = e.target;
    setFormulaireAdd(prev => ({ ...prev, [name]: value }));
  };

  const gererChangementEdit = (e) => {
    const { name, value } = e.target;
    setFormulaireEdit(prev => ({ ...prev, [name]: value }));
  };

 
const soumettreFormulaireAdd = async (e) => {
  e.preventDefault();
  try {
    if (!formulaireAdd.email.trim()) {
      alert("L'email est requis.");
      return;
    }

    const res = await axios.post('http://localhost:9000/users/add', {
      name: formulaireAdd.name,
      email: formulaireAdd.email,
      mdp: formulaireAdd.password, // <-- CHANGEMENT ici
    });

    setUtilisateurs([...utilisateurs, res.data]);
    setAfficherFormulaireAdd(false);
    setFormulaireAdd({ email: '', name: '', password: '' });

  } catch (error) {
    console.error("Erreur lors de l'ajout :", error);
    alert("Erreur lors de l'ajout !");
  }
};


  const soumettreFormulaireEdit = async (e) => {
    e.preventDefault();
    try {
      if (!formulaireEdit.email.trim()) {
        alert("L'email est requis pour identifier l'utilisateur à modifier.");
        return;
      }
  
      // 1. Récupération par email
      const res = await axios.get(`http://localhost:9000/users/get/${formulaireEdit.email}`);
      const utilisateurExistant = res.data;
  
      if (!utilisateurExistant || !utilisateurExistant.id) {
        alert("Utilisateur introuvable !");
        return;
      }
  
      // 2. Construction de l'objet de mise à jour
      const donneesMiseAJour = {
        id: utilisateurExistant.id,
        email: formulaireEdit.email,
        name: formulaireEdit.name,
        mdp: formulaireEdit.password,
      };
  
      // 3. Requête PUT
      await axios.put(`http://localhost:9000/users/${utilisateurExistant.id}`, donneesMiseAJour);
  
      // 4. Mise à jour de l'état local
      const misAJour = [...utilisateurs];
      misAJour[indexEdition] = { ...utilisateurExistant, ...donneesMiseAJour };
      setUtilisateurs(misAJour);
  
      // 5. Réinitialisation des formulaires
      setAfficherFormulaireEdit(false);
      setIndexEdition(null);
      setFormulaireEdit({ name: '', email: '', password: '' });
  
    } catch (error) {
      console.error("Erreur lors de la modification :", error);
      alert("Erreur lors de la mise à jour !");
    }
  };

  const rechercherUtilisateur = async () => {
    if (!recherche.trim()) {
      alert("Veuillez entrer un email à rechercher.");
      return;
    }
    try {
      const res = await axios.get(`http://localhost:9000/users/get/${recherche}`);
      if (res.data) {
        setUtilisateurRecherche(res.data);
      } else {
        alert("Aucun utilisateur trouvé.");
        setUtilisateurRecherche(null);
      }
    } catch (error) {
      alert("Utilisateur introuvable !");
      setUtilisateurRecherche(null);
    }
  };

  return (
    <div className="app">
      <aside className="sidebar">
        <div className="logo1">
          <a href="/">
            <img src={logo} alt="Properlize" width={120} />
          </a>
        </div>
        <ul>
          <li className="active">
            <FaUser /> UserList
          </li>
          <li>
            <FaSignOutAlt /> Logout
          </li>
        </ul>
      </aside>

      <main className="main">
        <header className="header">
          <span>Welcome Eric</span>
          <div className="header-icons">
            <img src={Notification} alt="Notifications" width={40} height={40} />
            <img src={avatar} alt="avatar" className="avatar" />
          </div>
        </header>

        <div className="userlist">
          <div className="userlist-header">
            <h2>UserList</h2>
            <div className="left">
              <div className="search-bar">
                <input
                  type="text"
                  placeholder="Rechercher par email"
                  value={recherche}
                  onChange={(e) => setRecherche(e.target.value)}
                />
                <div className='Violet' onClick={rechercherUtilisateur}>
                  <FaSearch />
                </div>
              </div>
              <button className="add-user" onClick={clicAjouterUtilisateur}>+ ADD</button>
              {utilisateursSelectionnes.length > 0 && (
                <button className="add-user" onClick={supprimerUtilisateursSelectionnes}>Delete All</button>
              )}
            </div>
          </div>

          {utilisateurRecherche && (
            <div className="user-search-result">
              <h3>Search Result :</h3>
              <table>
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Email</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>{utilisateurRecherche.name}</td>
                    <td>{utilisateurRecherche.email}</td>
                  </tr>
                </tbody>
              </table>
              <button onClick={() => setUtilisateurRecherche(null)}>close</button>
            </div>
          )}

          {afficherFormulaireAdd && (
            <form className="user-form" onSubmit={soumettreFormulaireAdd}>
              <input
                type="text"
                name="name"
                placeholder="Nom"
                value={formulaireAdd.name}
                onChange={gererChangementAdd}
                required
              />
              <input
                type="email"
                name="email"
                placeholder="Email"
                value={formulaireAdd.email}
                onChange={gererChangementAdd}
                required
              />
              <input
                type="password"
                name="password"
                placeholder="Password"
                value={formulaireAdd.password}
                onChange={gererChangementAdd}
                required
              />
              <button type="submit" className="add-user">
                Ajouter
              </button>
            </form>
          )}

          {afficherFormulaireEdit && (
            <form className="user-form" onSubmit={soumettreFormulaireEdit}>
              <input
                type="text"
                name="name"
                placeholder="Nom"
                value={formulaireEdit.name}
                onChange={gererChangementEdit}
                required
              />
              <input
                type="email"
                name="email"
                placeholder="Email"
                value={formulaireEdit.email}
                onChange={gererChangementEdit}
                required
              />
              <input
                type="password"
                name="password"
                placeholder="Password"
                value={formulaireEdit.password}
                onChange={gererChangementEdit}
                required
              />
              <button type="submit" className="add-user">
                Modifier
              </button>
            </form>
          )}

          <table>
            <thead>
              <tr>
                <th>
                  <input
                    type="checkbox"
                    className="checkbox"
                    checked={toutSelectionner}
                    onChange={basculerSelection}
                  />
                </th>
                <th>Name</th>
                <th>Email</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {utilisateurs.map((user, index) => (
                <tr key={user.id || index} className={utilisateursSelectionnes.includes(index) ? "highlight" : ""}>
                  <td>
                    <input
                      type="checkbox"
                      className="checkbox"
                      checked={utilisateursSelectionnes.includes(index)}
                      onChange={() => selectionnerUtilisateur(index)}
                    />
                  </td>
                  <td>{user.name}</td>
                  <td>{user.email}</td>
                  <td className="Edit">
                    <FaPen className="icon edit" onClick={() => modifierUtilisateur(index)} />
                    <FaTrash className="icon delete" onClick={() => supprimerUtilisateur(index)} />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <div className="footer">
            Total Number of users : <b>{utilisateurs.length}</b>
          </div>
        </div>
      </main>
    </div>
  );
}

export default App;
