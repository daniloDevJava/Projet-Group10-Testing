import React, { useState, useEffect } from 'react';
import '../style/UserListUI.css'; 
import Notification from '../assets/Notification.svg';
import logo from '../assets/logo.svg';
import avatar from "../assets/placeholder.svg";
import { FaUser, FaSignOutAlt, FaSearch, FaBell, FaPen, FaTrash } from "react-icons/fa";
//import axios from 'axios'; // Pour AJAX futur

function App() {
  const [users, setUsers] = useState([]);
  const [selectedUsers, setSelectedUsers] = useState([]);
  const [selectAll, setSelectAll] = useState(false);

  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ name: '', email: '', status: 'Actif' });
  const [editingIndex, setEditingIndex] = useState(null);

  // Simulation de récupération des données (à remplacer par un GET AJAX)
  useEffect(() => {
    // axios.get('/api/users')
    //   .then(res => setUsers(res.data))
    //   .catch(err => console.error(err));

    const initialUsers = [
      { name: "John Smith", email: "JohnSmith@gmail.com", status: "Actif" },
      { name: "Jane Doe", email: "JaneDoe@gmail.com", status: "Inactif" },
      { name: "Alice Brown", email: "AliceB@gmail.com", status: "Actif" }
    ];
    setUsers(initialUsers);
  }, []);

  const toggleSelectAll = () => {
    if (selectAll) {
      setSelectedUsers([]);
    } else {
      setSelectedUsers(users.map((_, index) => index));
    }
    setSelectAll(!selectAll);
  };

  const toggleUserSelect = (index) => {
    if (selectedUsers.includes(index)) {
      setSelectedUsers(selectedUsers.filter((i) => i !== index));
    } else {
      setSelectedUsers([...selectedUsers, index]);
    }
  };

  const deleteUser = (index) => {
    // axios.delete(`/api/users/${users[index].id}`)
    const updatedUsers = users.filter((_, i) => i !== index);
    setUsers(updatedUsers);
    setSelectedUsers(selectedUsers.filter((i) => i !== index));
  };

  const deleteSelectedUsers = () => {
    // axios.post('/api/users/delete-multiple', { ids: selectedUsers })
    const updatedUsers = users.filter((_, i) => !selectedUsers.includes(i));
    setUsers(updatedUsers);
    setSelectedUsers([]);
    setSelectAll(false);
  };

  const handleAddUserClick = () => {
    setForm({ name: '', email: '', status: 'Actif' });
    setEditingIndex(null);
    setShowForm(true);
  };

  const handleEditUser = (index) => {
    setForm(users[index]);
    setEditingIndex(index);
    setShowForm(true);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (editingIndex !== null) {
      // axios.put(`/api/users/${users[editingIndex].id}`, form)
      const updated = [...users];
      updated[editingIndex] = form;
      setUsers(updated);
    } else {
      // axios.post('/api/users', form)
      setUsers([...users, form]);
    }
    setShowForm(false);
    setForm({ name: '', email: '', status: 'Actif' });
    setEditingIndex(null);
  };

  return (
    <div className="app">
      <aside className="sidebar">
        <div className="logo1">
          <a href="/#">
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
          <span>Welcome Back Eric</span>
          <div className="header-icons">
            <img src={Notification} alt="Icone des notifications" width={40} height={40} />
            <img src={avatar} alt="avatar" className="avatar" />
          </div>
        </header>

        <div className="userlist">
          <div className="userlist-header">
            <h2>UserList</h2>
            <div className="left">
              <div className="search-bar">
                <input type="text" placeholder="Find an User" />
                <div  className='Violet'>
                  <FaSearch />
                </div>
              </div>
              <button className="add-user" onClick={handleAddUserClick}>+ ADD USER</button>
              {selectedUsers.length > 0 && (
                <button className="add-user" onClick={deleteSelectedUsers}>Delete All</button>
              )}
            </div>
          </div>

          {showForm && (
            <form className="user-form" onSubmit={handleSubmit}>
              <input
                type="text"
                name="name"
                placeholder="Nom"
                value={form.name}
                onChange={handleChange}
                required
              />
              <input
                type="email"
                name="email"
                placeholder="Email"
                value={form.email}
                onChange={handleChange}
                required
              />
              <select name="status" value={form.status} onChange={handleChange}>
                <option value="Actif">Actif</option>
                <option value="Inactif">Inactif</option>
              </select>
              <button type="submit" className="add-user">
                {editingIndex !== null ? 'Modifier' : 'Ajouter'}
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
                    checked={selectAll}
                    onChange={toggleSelectAll}
                  />
                </th>
                <th>Name</th>
                <th>Email</th>
                <th>Status</th>
                <th>Edit</th>
              </tr>
            </thead>

            <tbody>
              {users.map((user, index) => (
                <tr
                  key={index}
                  className={selectedUsers.includes(index) ? "highlight" : ""}
                >
                  <td>
                    <input
                      type="checkbox"
                      className="checkbox"
                      checked={selectedUsers.includes(index)}
                      onChange={() => toggleUserSelect(index)}
                    />
                  </td>
                  <td>{user.name}</td>
                  <td>{user.email}</td>
                  <td>{user.status}</td>
                  <td className="Edit">
                    <FaPen className="icon edit" onClick={() => handleEditUser(index)} />
                    <FaTrash className="icon delete" onClick={() => deleteUser(index)} />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <div className="footer">
            Total Number of Users <b>{users.length}</b>
          </div>
        </div>
      </main>
    </div>
  );
}

export default App;
