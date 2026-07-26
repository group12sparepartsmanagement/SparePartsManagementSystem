import React from 'react';
import { ShieldCheck, LogIn, UserPlus, Terminal, User, LogOut } from 'lucide-react';
import { getRoleBadge } from '../services/authService';

export default function Navbar({ activeTab, setActiveTab, currentUser, onLogout }) {
  const roleInfo = currentUser ? getRoleBadge(currentUser.role) : null;

  return (
    <header style={{
      background: 'rgba(11, 15, 25, 0.85)',
      backdropFilter: 'blur(12px)',
      borderBottom: '1px solid var(--border-color)',
      position: 'sticky',
      top: 0,
      zIndex: 100,
      padding: '14px 0'
    }}>
      <div className="container" style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '0 20px' }}>
        
        {/* Brand */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <div style={{
            width: '40px',
            height: '40px',
            borderRadius: '12px',
            background: 'linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            boxShadow: '0 0 15px rgba(99, 102, 241, 0.4)'
          }}>
            <ShieldCheck size={22} color="#fff" />
          </div>
          <div>
            <div style={{ fontWeight: 800, fontSize: '1.15rem', letterSpacing: '-0.3px', background: 'linear-gradient(90deg, #fff, #9ca3af)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>
              ASPMS Auth Portal
            </div>
            <div style={{ fontSize: '0.75rem', color: 'var(--accent-cyan)', display: 'flex', alignItems: 'center', gap: '6px' }}>
              <span style={{ width: '6px', height: '6px', borderRadius: '50%', background: '#10b981', display: 'inline-block' }}></span>
              Spring Boot Service (Port 8081)
            </div>
          </div>
        </div>

        {/* Navigation Actions */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <button
            className={`btn ${activeTab === 'console' ? 'btn-primary' : 'btn-secondary'}`}
            onClick={() => setActiveTab('console')}
          >
            <Terminal size={16} /> API Console
          </button>

          {currentUser ? (
            <>
              <button
                className={`btn ${activeTab === 'dashboard' ? 'btn-primary' : 'btn-secondary'}`}
                onClick={() => setActiveTab('dashboard')}
              >
                <User size={16} />
                <span>{currentUser.uname}</span>
                {roleInfo && <span className={`badge ${roleInfo.badgeClass}`} style={{ fontSize: '0.65rem' }}>{roleInfo.name}</span>}
              </button>

              <button className="btn btn-danger" onClick={onLogout} title="Logout">
                <LogOut size={16} />
              </button>
            </>
          ) : (
            <>
              <button
                className={`btn ${activeTab === 'login' ? 'btn-primary' : 'btn-secondary'}`}
                onClick={() => setActiveTab('login')}
              >
                <LogIn size={16} /> Sign In
              </button>

              <button
                className={`btn ${activeTab === 'register' ? 'btn-primary' : 'btn-secondary'}`}
                onClick={() => setActiveTab('register')}
              >
                <UserPlus size={16} /> Register
              </button>
            </>
          )}
        </div>

      </div>
    </header>
  );
}
