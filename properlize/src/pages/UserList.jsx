import React, { useState } from 'react';
import '../style/UserListUI.css'; 

const UserListUI = () => {
  const [selectedUsers, setSelectedUsers] = useState([1]);
  const [searchTerm, setSearchTerm] = useState('');

  const users = Array(9).fill(null).map((_, index) => ({
    id: index,
    name: 'John Smith',
    email: 'JohnSmith@gmail.com',
    status: 'Actif'
  }));

  const toggleUserSelection = (userId) => {
    setSelectedUsers(prev => 
      prev.includes(userId) 
        ? prev.filter(id => id !== userId)
        : [...prev, userId]
    );
  };

  const toggleSelectAll = () => {
    setSelectedUsers(prev => 
      prev.length === users.length ? [] : users.map(user => user.id)
    );
  };

  return (
    <div className="app-container">
      {/* Sidebar */}
      <div className="sidebar">
        {/* Header */}
        <div className="sidebar-header">
          <h1 className="logo">
            <span className="logo-highlight">Pro</span>perlize
          </h1>
        </div>

        {/* Navigation */}
        <nav className="sidebar-nav">
          <div className="welcome-message">
            Welcome Back Eric
          </div>
          
          <div className="nav-items">
            <div className="nav-item active">
              <svg className="nav-icon" viewBox="0 0 20 20">
                <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              UserList
            </div>
            
            <div className="nav-item">
              <svg className="nav-icon" viewBox="0 0 20 20">
                <path fillRule="evenodd" d="M3 3a1 1 0 00-1 1v12a1 1 0 102 0V4a1 1 0 00-1-1zm10.293 9.293a1 1 0 001.414 1.414l3-3a1 1 0 000-1.414l-3-3a1 1 0 10-1.414 1.414L14.586 9H7a1 1 0 100 2h7.586l-1.293 1.293z" clipRule="evenodd" />
              </svg>
              Logout
            </div>
          </div>
        </nav>
      </div>

      {/* Main Content */}
      <div className="main-content">
        {/* Header */}
        <div className="content-header">
          <h2 className="content-title">UserList</h2>
          <div className="header-actions">
            <div className="search-container">
              <input
                type="text"
                placeholder="Find an User"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="search-input"
              />
              <button className="search-button">
                <svg className="search-icon" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clipRule="evenodd" />
                </svg>
              </button>
            </div>
            <button className="add-user-button">
              <span className="plus-icon">+</span>
              <span>ADD USER</span>
            </button>
          </div>
        </div>

        {/* User Table */}
        <div className="user-table-container">
          <table className="user-table">
            <thead className="table-header">
              <tr>
                <th className="checkbox-cell">
                  <input
                    type="checkbox"
                    checked={selectedUsers.length === users.length}
                    onChange={toggleSelectAll}
                    className="checkbox-input"
                  />
                </th>
                <th className="table-heading">
                  Name
                </th>
                <th className="table-heading">
                  Email
                </th>
                <th className="table-heading">
                  Status
                </th>
                <th className="table-heading">
                  Edit
                </th>
              </tr>
            </thead>
            <tbody className="table-body">
              {users.map((user) => (
                <tr 
                  key={user.id} 
                  className={selectedUsers.includes(user.id) ? 'selected-row' : 'table-row'}
                >
                  <td className="checkbox-cell">
                    <input
                      type="checkbox"
                      checked={selectedUsers.includes(user.id)}
                      onChange={() => toggleUserSelection(user.id)}
                      className="checkbox-input"
                    />
                  </td>
                  <td className="user-name">
                    {user.name}
                  </td>
                  <td className="user-email">
                    {user.email}
                  </td>
                  <td className="user-status">
                    {user.status}
                  </td>
                  <td className="action-cell">
                    <div className="action-buttons">
                      <button className="edit-button">
                        <svg className="action-icon" viewBox="0 0 20 20">
                          <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
                        </svg>
                      </button>
                      <button className="delete-button">
                        <svg className="action-icon" viewBox="0 0 20 20">
                          <path fillRule="evenodd" d="M9 2a1 1 0 000 2h2a1 1 0 100-2H9z" clipRule="evenodd" />
                          <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                        </svg>
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Footer */}
        <div className="table-footer">
          <div className="footer-text">
            Total Number of Users
          </div>
          <div className="footer-count">
            50
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserListUI;