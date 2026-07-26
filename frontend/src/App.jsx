import React, { useState, useEffect } from 'react';
import Navbar from './components/Navbar';
import LoginModal from './components/LoginModal';
import RegisterModal from './components/RegisterModal';
import Dashboard from './components/Dashboard';
import ApiConsole from './components/ApiConsole';
import Toast from './components/Toast';

export default function App() {
  const [currentUser, setCurrentUser] = useState(() => {
    try {
      const saved = localStorage.getItem('aspms_auth_user');
      return saved ? JSON.parse(saved) : null;
    } catch (e) {
      return null;
    }
  });

  const [activeTab, setActiveTab] = useState(() => (currentUser ? 'dashboard' : 'login'));
  const [toasts, setToasts] = useState([]);

  useEffect(() => {
    if (currentUser) {
      localStorage.setItem('aspms_auth_user', JSON.stringify(currentUser));
    } else {
      localStorage.removeItem('aspms_auth_user');
    }
  }, [currentUser]);

  const addToast = (type, message) => {
    const id = Date.now() + Math.random();
    setToasts((prev) => [...prev, { id, type, message }]);

    setTimeout(() => {
      removeToast(id);
    }, 4000);
  };

  const removeToast = (id) => {
    setToasts((prev) => prev.filter((t) => t.id !== id));
  };

  const handleLoginSuccess = (userObj) => {
    setCurrentUser(userObj);
    setActiveTab('dashboard');
  };

  const handleRegisterSuccess = (userObj) => {
    setCurrentUser(userObj);
    setActiveTab('dashboard');
  };

  const handleLogout = () => {
    setCurrentUser(null);
    setActiveTab('login');
    addToast('info', 'Logged out successfully');
  };

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      <Navbar
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        currentUser={currentUser}
        onLogout={handleLogout}
      />

      <main className="container" style={{ flex: 1, paddingTop: '32px', paddingBottom: '40px' }}>
        {activeTab === 'dashboard' && currentUser && (
          <Dashboard user={currentUser} onLogout={handleLogout} setActiveTab={setActiveTab} />
        )}

        {activeTab === 'login' && (
          <LoginModal
            onSuccess={handleLoginSuccess}
            addToast={addToast}
            switchToRegister={() => setActiveTab('register')}
          />
        )}

        {activeTab === 'register' && (
          <RegisterModal
            onSuccess={handleRegisterSuccess}
            addToast={addToast}
            switchToLogin={() => setActiveTab('login')}
          />
        )}

        {activeTab === 'console' && (
          <ApiConsole addToast={addToast} />
        )}
      </main>

      <Toast toasts={toasts} removeToast={removeToast} />

      <footer style={{
        textAlign: 'center',
        padding: '20px',
        color: 'var(--text-muted)',
        fontSize: '0.8rem',
        borderTop: '1px solid var(--border-color)',
        marginTop: 'auto'
      }}>
        ASPMS Lite Microservices Platform • Auth Service API Client v1.0
      </footer>
    </div>
  );
}
