import React, { useState, useEffect } from 'react';
import '../style/UserListUI.css';
import Notification from '../assets/Notification.svg';
import logo from '../assets/logo.svg';
import avatar from "../assets/placeholder.svg";
import { FaUser, FaSignOutAlt, FaSearch, FaBell, FaPen, FaTrash } from "react-icons/fa";
import axios from 'axios';

function UserList() {
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
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  useEffect(() => {
    chargerUtilisateurs();
  }, []);

  const chargerUtilisateurs = async () => {
    setLoading(true);
    try {
      const response = await axios.get('http://localhost:9000/users/all');
      setUtilisateurs(response.data);
      setUtilisateurRecherche(null);
      setMessage('');
    } catch (error) {
      console.error("Erreur lors du chargement des utilisateurs :", error);
      setMessage('Erreur lors du chargement des utilisateurs');
    } finally {
      setLoading(false);
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
    setLoading(true);
    try {
      await axios.delete(`http://localhost:9000/users/${utilisateur.id}`);
      const misAJour = utilisateurs.filter((_, i) => i !== index);
      setUtilisateurs(misAJour);
      setUtilisateursSelectionnes(utilisateursSelectionnes.filter((i) => i !== index));
      setMessage('Utilisateur supprimé avec succès');
    } catch (error) {
      console.error("Erreur lors de la suppression :", error);
      setMessage('Erreur lors de la suppression');
    } finally {
      setLoading(false);
    }
  };

  const supprimerUtilisateursSelectionnes = async () => {
    setLoading(true);
    try {
      for (const index of utilisateursSelectionnes) {
        await supprimerUtilisateur(index);
      }
      setToutSelectionner(false);
      setMessage('Utilisateurs supprimés avec succès');
    } catch (error) {
      console.error("Erreur lors de la suppression multiple :", error);
      setMessage('Erreur lors de la suppression multiple');
    } finally {
      setLoading(false);
    }
  };

  const clicAjouterUtilisateur = () => {
    setFormulaireAdd({ name: '', email: '', password: '' });
    setAfficherFormulaireAdd(true);
    setAfficherFormulaireEdit(false);
    setMessage('');
  };

  const modifierUtilisateur = (index) => {
    const user = utilisateurs[index];
    setFormulaireEdit({ name: user.name, email: user.email, password: '' });
    setIndexEdition(index);
    setAfficherFormulaireEdit(true);
    setAfficherFormulaireAdd(false);
    setMessage('');
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
    setLoading(true);
    try {
      if (!formulaireAdd.email.trim()) {
        setMessage("L'email est requis.");
        return;
      }

      const res = await axios.post('http://localhost:9000/users/add', {
        name: formulaireAdd.name,
        email: formulaireAdd.email,
        mdp: formulaireAdd.password,
      });

      setUtilisateurs([...utilisateurs, res.data]);
      setAfficherFormulaireAdd(false);
      setFormulaireAdd({ email: '', name: '', password: '' });
      setMessage('Utilisateur ajouté avec succès');
    } catch (error) {
      console.error("Erreur lors de l'ajout :", error);
      setMessage("Erreur lors de l'ajout !");
    } finally {
      setLoading(false);
    }
  };

  const soumettreFormulaireEdit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      if (!formulaireEdit.email.trim()) {
        setMessage("L'email est requis pour identifier l'utilisateur à modifier.");
        return;
      }

      const res = await axios.get(`http://localhost:9000/users/get/${formulaireEdit.email}`);
      const utilisateurExistant = res.data;

      if (!utilisateurExistant || !utilisateurExistant.id) {
        setMessage("Utilisateur introuvable !");
        return;
      }

      const donneesMiseAJour = {
        id: utilisateurExistant.id,
        email: formulaireEdit.email,
        name: formulaireEdit.name,
        mdp: formulaireEdit.password,
      };

      await axios.put(`http://localhost:9000/users/${utilisateurExistant.id}`, donneesMiseAJour);

      const misAJour = [...utilisateurs];
      misAJour[indexEdition] = { ...utilisateurExistant, ...donneesMiseAJour };
      setUtilisateurs(misAJour);

      setAfficherFormulaireEdit(false);
      setIndexEdition(null);
      setFormulaireEdit({ name: '', email: '', password: '' });
      setMessage('Utilisateur modifié avec succès');
    } catch (error) {
      console.error("Erreur lors de la modification :", error);
      setMessage("Erreur lors de la mise à jour !");
    } finally {
      setLoading(false);
    }
  };

  const rechercherUtilisateur = async () => {
    if (!recherche.trim()) {
      setMessage("Veuillez entrer un email à rechercher.");
      return;
    }
    setLoading(true);
    try {
      const res = await axios.get(`http://localhost:9000/users/get/${recherche}`);
      if (res.data) {
        setUtilisateurRecherche(res.data);
        setMessage('');
      } else {
        setMessage("Aucun utilisateur trouvé.");
        setUtilisateurRecherche(null);
      }
    } catch (error) {
      setMessage("Utilisateur introuvable !");
      setUtilisateurRecherche(null);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app" data-testid="user-list-page">
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
            <h1 role="heading" aria-level="1">UserList</h1>
            <div className="left">
              <div className="search-bar">
                <input
                  type="text"
                  placeholder="Rechercher par email"
                  aria-label="Rechercher par email"
                  value={recherche}
                  onChange={(e) => setRecherche(e.target.value)}
                  data-testid="search-input"
                />
                <button 
                  className='Violet' 
                  onClick={rechercherUtilisateur}
                  aria-label="Rechercher"
                  data-testid="search-button"
                >
                  <FaSearch />
                </button>
              </div>
              <button 
                className="add-user" 
                onClick={clicAjouterUtilisateur}
                aria-label="Ajouter un utilisateur"
                data-testid="add-user-button"
              >
                + ADD
              </button>
              {utilisateursSelectionnes.length > 0 && (
                <button 
                  className="add-user" 
                  onClick={supprimerUtilisateursSelectionnes}
                  data-testid="delete-selected-button"
                >
                  Delete All
                </button>
              )}
            </div>
          </div>

          {message && <div className="message" data-testid="message">{message}</div>}
          {loading && <div className="loading" data-testid="loading">Chargement...</div>}

          {utilisateurRecherche && (
            <div className="user-search-result" data-testid="search-results">
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
              <button 
                onClick={() => setUtilisateurRecherche(null)}
                data-testid="close-search-button"
              >
                close
              </button>
            </div>
          )}

          {afficherFormulaireAdd && (
            <form 
              className="user-form" 
              onSubmit={soumettreFormulaireAdd}
              data-testid="add-user-form"
              aria-label="Formulaire d'ajout d'utilisateur"
            >
              <input
                type="text"
                name="name"
                placeholder="Nom complet"
                value={formulaireAdd.name}
                onChange={gererChangementAdd}
                required
                data-testid="name-input"
              />
              <input
                type="email"
                name="email"
                placeholder="Email"
                value={formulaireAdd.email}
                onChange={gererChangementAdd}
                required
                data-testid="email-input"
              />
              <input
                type="password"
                name="password"
                placeholder="Mot de passe"
                value={formulaireAdd.password}
                onChange={gererChangementAdd}
                required
                data-testid="password-input"
              />
              <button 
                type="submit" 
                className="add-user"
                data-testid="submit-add-button"
              >
                Ajouter
              </button>
            </form>
          )}

          {afficherFormulaireEdit && (
            <form 
              className="user-form" 
              onSubmit={soumettreFormulaireEdit}
              data-testid="edit-user-form"
              aria-label="Formulaire de modification d'utilisateur"
            >
              <input
                type="text"
                name="name"
                placeholder="Nom complet"
                value={formulaireEdit.name}
                onChange={gererChangementEdit}
                required
                data-testid="edit-name-input"
              />
              <input
                type="email"
                name="email"
                placeholder="Email"
                value={formulaireEdit.email}
                onChange={gererChangementEdit}
                required
                data-testid="edit-email-input"
              />
              <input
                type="password"
                name="password"
                placeholder="Mot de passe"
                value={formulaireEdit.password}
                onChange={gererChangementEdit}
                required
                data-testid="edit-password-input"
              />
              <button 
                type="submit" 
                className="add-user"
                data-testid="submit-edit-button"
              >
                Modifier
              </button>
            </form>
          )}

          <table data-testid="users-table">
            <thead>
              <tr>
                <th>
                  <input 
                    type="checkbox" 
                    className="checkbox" 
                    checked={toutSelectionner} 
                    onChange={basculerSelection}
                    data-testid="select-all-checkbox"
                  />
                </th>
                <th>Name</th>
                <th>Email</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {utilisateurs.map((user, index) => (
                <tr 
                  key={user.id || index} 
                  className={utilisateursSelectionnes.includes(index) ? "highlight" : ""}
                  data-testid={`user-row-${index}`}
                >
                  <td>
                    <input 
                      type="checkbox" 
                      className="checkbox" 
                      checked={utilisateursSelectionnes.includes(index)} 
                      onChange={() => selectionnerUtilisateur(index)}
                      data-testid={`select-user-${index}`}
                    />
                  </td>
                  <td data-testid="user-name">{user.name}</td>
                  <td data-testid="user-email">{user.email}</td>
                  <td className="Edit">
                    <FaPen 
                      className="icon edit" 
                      onClick={() => modifierUtilisateur(index)}
                      role="button"
                      aria-label={`Modifier ${user.name}`}
                      data-testid={`edit-user-${index}`}
                    />
                    <FaTrash 
                      className="icon delete" 
                      onClick={() => supprimerUtilisateur(index)}
                      role="button"
                      aria-label={`Supprimer ${user.name}`}
                      data-testid={`delete-user-${index}`}
                    />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <div className="footer" data-testid="user-count">
            Total Number of users : <b>{utilisateurs.length}</b>
          </div>
        </div>
      </main>
    </div>
  );
}

export default UserList;