import React, { useState } from 'react';
import { UserPlus, User, Key, MapPin, Shield, AlertCircle, ArrowRight } from 'lucide-react';
import { registerUser } from '../services/authService';

export default function RegisterModal({ onSuccess, addToast, switchToLogin }) {
  const [uname, setUname] = useState('');
  const [password, setPassword] = useState('');
  const [address, setAddress] = useState('');
  const [rid, setRid] = useState(3);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!uname.trim() || !password.trim()) {
      setErrorMessage('Username and password are required.');
      return;
    }

    if (password.length < 6) {
      setErrorMessage('Password must be at least 6 characters long.');
      return;
    }

    setErrorMessage('');
    setLoading(true);

    const payload = {
      uname: uname.trim(),
      password,
      address: address.trim() || null,
      rid: Number(rid),
    };

    const result = await registerUser(payload);
    setLoading(false);

    if (result.success) {
      const responseBody = result.data;
      addToast('success', responseBody.message || 'Registration successful!');
      if (responseBody.data) {
        onSuccess(responseBody.data);
      } else {
        switchToLogin();
      }
    } else {
      const msg = result.error?.message || result.error?.errors?.[0]?.defaultMessage || 'Registration failed';
      setErrorMessage(msg);
      addToast('error', msg);
    }
  };

  return (
    <div className="glass-card fade-in" style={{ padding: '32px', maxWidth: '480px', margin: '40px auto' }}>
      <div style={{ textAlign: 'center', marginBottom: '24px' }}>
        <div style={{
          width: '48px',
          height: '48px',
          borderRadius: '50%',
          background: 'rgba(139, 92, 246, 0.15)',
          color: 'var(--accent-purple)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          margin: '0 auto 12px'
        }}>
          <UserPlus size={24} />
        </div>
        <h2 style={{ fontSize: '1.4rem', fontWeight: 700 }}>Create New Account</h2>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.88rem', marginTop: '4px' }}>
          Register a user profile with role assignment
        </p>
      </div>

      {errorMessage && (
        <div style={{
          background: 'rgba(244, 63, 94, 0.1)',
          border: '1px solid rgba(244, 63, 94, 0.3)',
          borderRadius: 'var(--radius-sm)',
          padding: '12px 14px',
          color: '#fda4af',
          fontSize: '0.85rem',
          display: 'flex',
          alignItems: 'center',
          gap: '8px',
          marginBottom: '18px'
        }}>
          <AlertCircle size={16} />
          <span>{errorMessage}</span>
        </div>
      )}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label className="form-label">Username *</label>
          <div style={{ position: 'relative' }}>
            <input
              type="text"
              className="form-input"
              placeholder="e.g. john_doe"
              value={uname}
              onChange={(e) => setUname(e.target.value)}
              disabled={loading}
              style={{ paddingLeft: '40px' }}
              required
            />
            <User size={16} style={{ position: 'absolute', left: '14px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
          </div>
        </div>

        <div className="form-group">
          <label className="form-label">Password * (min 6 characters)</label>
          <div style={{ position: 'relative' }}>
            <input
              type="password"
              className="form-input"
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              disabled={loading}
              style={{ paddingLeft: '40px' }}
              required
            />
            <Key size={16} style={{ position: 'absolute', left: '14px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
          </div>
        </div>

        <div className="form-group">
          <label className="form-label">Address (Optional)</label>
          <div style={{ position: 'relative' }}>
            <input
              type="text"
              className="form-input"
              placeholder="e.g. Pune, Maharashtra"
              value={address}
              onChange={(e) => setAddress(e.target.value)}
              disabled={loading}
              style={{ paddingLeft: '40px' }}
            />
            <MapPin size={16} style={{ position: 'absolute', left: '14px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
          </div>
        </div>

        <div className="form-group">
          <label className="form-label">Select User Role (rid) *</label>
          <div style={{ position: 'relative' }}>
            <select
              className="form-select"
              value={rid}
              onChange={(e) => setRid(e.target.value)}
              disabled={loading}
              style={{ paddingLeft: '40px' }}
            >
              <option value={3}>Customer (Role ID: 3)</option>
              <option value={2}>Supplier (Role ID: 2)</option>
              <option value={4}>Staff (Role ID: 4)</option>
              <option value={1}>Admin (Role ID: 1)</option>
            </select>
            <Shield size={16} style={{ position: 'absolute', left: '14px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
          </div>
        </div>

        <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: '12px', padding: '12px' }} disabled={loading}>
          {loading ? 'Creating Account...' : (
            <>
              Complete Registration <ArrowRight size={16} />
            </>
          )}
        </button>
      </form>

      <div style={{ textAlign: 'center', marginTop: '18px', fontSize: '0.85rem', color: 'var(--text-muted)' }}>
        Already registered?{' '}
        <button
          onClick={switchToLogin}
          style={{ background: 'none', border: 'none', color: 'var(--primary)', fontWeight: 600, cursor: 'pointer', textDecoration: 'underline' }}
        >
          Sign In here
        </button>
      </div>
    </div>
  );
}
