import React from 'react';
import { User, Shield, MapPin, Hash, CheckCircle2, LogOut, Code, Server } from 'lucide-react';
import { getRoleBadge } from '../services/authService';

export default function Dashboard({ user, onLogout, setActiveTab }) {
  if (!user) return null;

  const roleInfo = getRoleBadge(user.role);

  return (
    <div className="fade-in" style={{ maxWidth: '900px', margin: '0 auto', padding: '20px 0' }}>
      
      {/* Header Banner */}
      <div className="glass-card" style={{ padding: '32px', marginBottom: '24px', position: 'relative', overflow: 'hidden' }}>
        <div style={{
          position: 'absolute',
          right: '-20px',
          top: '-20px',
          width: '180px',
          height: '180px',
          background: 'radial-gradient(circle, rgba(99, 102, 241, 0.2) 0%, transparent 70%)',
          borderRadius: '50%'
        }} />
        
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '20px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
            <div style={{
              width: '64px',
              height: '64px',
              borderRadius: '20px',
              background: 'linear-gradient(135deg, var(--primary) 0%, var(--accent-purple) 100%)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              fontSize: '1.8rem',
              fontWeight: 'bold',
              color: '#fff',
              boxShadow: '0 8px 20px var(--primary-glow)'
            }}>
              {user.uname?.charAt(0).toUpperCase() || 'U'}
            </div>
            
            <div>
              <div style={{ display: 'flex', alignItems: 'center', gap: '10px', flexWrap: 'wrap' }}>
                <h1 style={{ fontSize: '1.6rem', fontWeight: 800 }}>{user.uname}</h1>
                <span className={`badge ${roleInfo.badgeClass}`}>{roleInfo.name}</span>
              </div>
              <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginTop: '4px', display: 'flex', alignItems: 'center', gap: '6px' }}>
                <CheckCircle2 size={15} className="text-emerald-400" /> Active Authenticated Session
              </p>
            </div>
          </div>

          <button className="btn btn-danger" onClick={onLogout}>
            <LogOut size={16} /> End Session
          </button>
        </div>
      </div>

      {/* Grid Overview Cards */}
      <div className="grid-2" style={{ marginBottom: '24px' }}>
        
        {/* Profile Card */}
        <div className="glass-card" style={{ padding: '24px' }}>
          <h3 style={{ fontSize: '1.05rem', fontWeight: 700, marginBottom: '16px', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <User size={18} color="var(--primary)" /> Profile Identity
          </h3>
          
          <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', paddingBottom: '10px', borderBottom: '1px solid var(--border-color)' }}>
              <span style={{ color: 'var(--text-muted)', fontSize: '0.88rem', display: 'flex', alignItems: 'center', gap: '6px' }}>
                <Hash size={14} /> User ID (uid)
              </span>
              <span style={{ fontFamily: 'var(--font-mono)', fontWeight: 600 }}>{user.uid || 'Generated on DB'}</span>
            </div>

            <div style={{ display: 'flex', justifyContent: 'space-between', paddingBottom: '10px', borderBottom: '1px solid var(--border-color)' }}>
              <span style={{ color: 'var(--text-muted)', fontSize: '0.88rem', display: 'flex', alignItems: 'center', gap: '6px' }}>
                <Shield size={14} /> Assigned Role
              </span>
              <span className={`badge ${roleInfo.badgeClass}`}>{roleInfo.name}</span>
            </div>

            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <span style={{ color: 'var(--text-muted)', fontSize: '0.88rem', display: 'flex', alignItems: 'center', gap: '6px' }}>
                <MapPin size={14} /> Address
              </span>
              <span style={{ fontWeight: 500 }}>{user.address || 'Not specified'}</span>
            </div>
          </div>
        </div>

        {/* Backend Info Card */}
        <div className="glass-card" style={{ padding: '24px' }}>
          <h3 style={{ fontSize: '1.05rem', fontWeight: 700, marginBottom: '16px', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Server size={18} color="var(--accent-cyan)" /> Service Telemetry
          </h3>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', paddingBottom: '10px', borderBottom: '1px solid var(--border-color)' }}>
              <span style={{ color: 'var(--text-muted)', fontSize: '0.88rem' }}>Backend Service</span>
              <span style={{ color: 'var(--accent-cyan)', fontWeight: 600 }}>ASPMS Auth Microservice</span>
            </div>

            <div style={{ display: 'flex', justifyContent: 'space-between', paddingBottom: '10px', borderBottom: '1px solid var(--border-color)' }}>
              <span style={{ color: 'var(--text-muted)', fontSize: '0.88rem' }}>Backend Port</span>
              <span style={{ fontFamily: 'var(--font-mono)', fontWeight: 600 }}>8081</span>
            </div>

            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <span style={{ color: 'var(--text-muted)', fontSize: '0.88rem' }}>Auth Mechanism</span>
              <span style={{ color: 'var(--accent-emerald)', fontWeight: 600 }}>BCrypt Password Encryption</span>
            </div>
          </div>
        </div>

      </div>

      {/* API Tester Quick Launch Card */}
      <div className="glass-card" style={{ padding: '24px', background: 'linear-gradient(135deg, rgba(17, 24, 39, 0.9) 0%, rgba(31, 41, 55, 0.7) 100%)' }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '16px' }}>
          <div>
            <h4 style={{ fontSize: '1.05rem', fontWeight: 700, display: 'flex', alignItems: 'center', gap: '8px' }}>
              <Code size={18} color="var(--accent-purple)" /> Test Backend Endpoints Directly
            </h4>
            <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem', marginTop: '4px' }}>
              Inspect raw HTTP request headers, body parameters, and Spring REST response JSON objects in real-time.
            </p>
          </div>
          <button className="btn btn-primary" onClick={() => setActiveTab('console')}>
            Open API Console
          </button>
        </div>
      </div>

    </div>
  );
}
