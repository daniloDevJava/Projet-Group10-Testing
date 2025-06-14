// Fichier : src/pages/UserList.jsx

import React, { useState, useEffect } from 'react';
import '../style/UserListUI.css';
import Notification from '../assets/Notification.svg';
import logo from '../assets/logo.svg';
import avatar from "../assets/placeholder.svg";
import { FaUser, FaSignOutAlt, FaSearch, FaPen, FaTrash } from "react-icons/fa";
import axios from 'axios';

function UserList() {
  const [utilisateurs, setUtilisateurs] = useState([]);
  const [utilisateursSelectionnes, setUtilisateursSelectionnes] = useState([]);
  const [toutSelectionner, setToutSelectionner] = useState(false);
  const [afficherFormulaireAdd, setAfficherFormulaireAdd] = useState(false);
  const [afficherFormulaireEdit, setAfficherFormulaireEdit] = useState(false);
  const [formulaireAdd, setFormulaireAdd] = useState({ name: '', email: '', password: '' });
  const [formulaireEdit, setFormulaireEdit] = useState({ id: null, name: '', email: '', password: '' });
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
      setUtilisateursSelectionnes(utilisateurs.map((user) => user.id));
    }
    setToutSelectionner(!toutSelectionner);
  };

  const selectionnerUtilisateur = (userId) => {
    if (utilisateursSelectionnes.includes(userId)) {
      setUtilisateursSelectionnes(utilisateursSelectionnes.filter((id) => id !== userId));
    } else {
      setUtilisateursSelectionnes([...utilisateursSelectionnes, userId]);
    }
  };

  const supprimerUtilisateur = async (userIdToDelete) => {
    try {
      await axios.delete(`http://localhost:9000/users/${userIdToDelete}`);
      setUtilisateurs(prevUsers => prevUsers.filter(user => user.id !== userIdToDelete));
      setUtilisateursSelectionnes(prevSelected => prevSelected.filter(id => id !== userIdToDelete));
    } catch (error) {
      console.error("Erreur lors de la suppression :", error);
    }
  };

  const supprimerUtilisateursSelectionnes = async () => {
    try {
        await Promise.all(
            utilisateursSelectionnes.map(userId => axios.delete(`http://localhost:9000/users/${userId}`))
        );
        chargerUtilisateurs(); // Recharger la liste depuis le serveur
        setUtilisateursSelectionnes([]);
        setToutSelectionner(false);
    } catch (error) {
        console.error("Erreur lors de la suppression multiple :", error);
    }
  };

  const clicAjouterUtilisateur = () => {
    setFormulaireAdd({ name: '', email: '', password: '' });
    setAfficherFormulaireAdd(true);
    setAfficherFormulaireEdit(false);
  };

  const modifierUtilisateur = (userToEdit, index) => {
    setFormulaireEdit({ id: userToEdit.id, name: userToEdit.name, email: userToEdit.email, password: '' });
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
        mdp: formulaireAdd.password,
      });
      setUtilisateurs([...utilisateurs, res.data]);
      setAfficherFormulaireAdd(false);
      setFormulaireAdd({ name: '', email: '', password: '' });
    } catch (error) {
      console.error("Erreur lors de l'ajout :", error);
      alert("Erreur lors de l'ajout !");
    }
  };

  const soumettreFormulaireEdit = async (e) => {
    e.preventDefault();
    const { id, name, email, password } = formulaireEdit;
    try {
      const donneesMiseAJour = { name, email };
      if (password) {
        donneesMiseAJour.mdp = password;
      }
      await axios.put(`http://localhost:9000/users/${id}`, donneesMiseAJour);
      const misAJour = [...utilisateurs];
      misAJour[indexEdition] = { ...misAJour[indexEdition], ...donneesMiseAJour };
      setUtilisateurs(misAJour);
      setAfficherFormulaireEdit(false);
      setIndexEdition(null);
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
      setUtilisateurRecherche(res.data || null);
      if (!res.data) {
        alert("Aucun utilisateur trouvé.");
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
          <li className="active"><FaUser /> UserList</li>
          <li><FaSignOutAlt /> Logout</li>
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
                <input type="text" placeholder="Rechercher par email" value={recherche} onChange={(e) => setRecherche(e.target.value)} />
                <div className='Violet' onClick={rechercherUtilisateur}><FaSearch /></div>
              </div>
              <button className="add-user" onClick={clicAjouterUtilisateur}>+ ADD</button>
              {utilisateursSelectionnes.length > 0 && (
                <button className="add-user" onClick={supprimerUtilisateursSelectionnes}>Delete All</button>
              )}
            </div>
          </div>

          {utilisateurRecherche && (
            <div className="user-search-result">
              <h3>Search Result:</h3>
              <table>
                <thead><tr><th>Name</th><th>Email</th></tr></thead>
                <tbody><tr><td>{utilisateurRecherche.name}</td><td>{utilisateurRecherche.email}</td></tr></tbody>
              </table>
              <button onClick={() => setUtilisateurRecherche(null)}>Close</button>
            </div>
          )}

          {afficherFormulaireAdd && (
            <form className="user-form" onSubmit={soumettreFormulaireAdd} data-testid="add-user-form">
              <input type="text" name="name" placeholder="Nom" value={formulaireAdd.name} onChange={gererChangementAdd} required />
              <input type="email" name="email" placeholder="Email" value={formulaireAdd.email} onChange={gererChangementAdd} required />
              <input type="password" name="password" placeholder="Password" value={formulaireAdd.password} onChange={gererChangementAdd} required />
              <button type="submit" className="add-user">Ajouter</button>
            </form>
          )}

          {afficherFormulaireEdit && (
            <form className="user-form" onSubmit={soumettreFormulaireEdit} data-testid="edit-user-form">
              <input type="text" name="name" placeholder="Nom" value={formulaireEdit.name} onChange={gererChangementEdit} required />
              <input type="email" name="email" placeholder="Email" value={formulaireEdit.email} onChange={gererChangementEdit} required />
              <input type="password" name="password" placeholder="Nouveau mot de passe (optionnel)" value={formulaireEdit.password} onChange={gererChangementEdit} />
              <button type="submit" className="add-user">Modifier</button>
            </form>
          )}

          <table>
            <thead>
              <tr>
                <th><input type="checkbox" className="checkbox" checked={toutSelectionner} onChange={basculerSelection} /></th>
                <th>Name</th>
                <th>Email</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {utilisateurs.map((user, index) => (
                <tr key={user.id} className={utilisateursSelectionnes.includes(user.id) ? "highlight" : ""}>
                  <td><input type="checkbox" className="checkbox" checked={utilisateursSelectionnes.includes(user.id)} onChange={() => selectionnerUtilisateur(user.id)} /></td>
                  <td>{user.name}</td>
                  <td>{user.email}</td>
                  <td className="Edit">
                    <FaPen className="icon edit" onClick={() => modifierUtilisateur(user, index)} />
                    <FaTrash className="icon delete" onClick={() => supprimerUtilisateur(user.id)} />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <div className="footer">
            Total Number of users: <b>{utilisateurs.length}</b>
          </div>
        </div>
      </main>
    </div>
  );
}

export default UserList;