import React, { useState } from 'react';
import { LogIn, Key, User, AlertCircle, ArrowRight, Sparkles } from 'lucide-react';
import { loginUser } from '../services/authService';

export default function LoginModal({ onSuccess, addToast, switchToRegister }) {
  const [uname, setUname] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!uname.trim() || !password.trim()) {
      setErrorMessage('Please enter both username and password.');
      return;
    }

    setErrorMessage('');
    setLoading(true);

    const result = await loginUser({ uname: uname.trim(), password });
    setLoading(false);

    if (result.success) {
      const responseBody = result.data;
      const userObj = responseBody.data || { uname, role: 'Customer' };
      addToast('success', responseBody.message || 'Login successful!');
      onSuccess(userObj);
    } else {
      const msg = result.error?.message || 'Invalid username or password';
      setErrorMessage(msg);
      addToast('error', msg);
    }
  };

  const handleQuickFill = (demoUname, demoPass) => {
    setUname(demoUname);
    setPassword(demoPass);
    setErrorMessage('');
  };

  return (
    <div className="glass-card fade-in" style={{ padding: '32px', maxWidth: '440px', margin: '40px auto' }}>
      <div style={{ textAlign: 'center', marginBottom: '24px' }}>
        <div style={{
          width: '48px',
          height: '48px',
          borderRadius: '50%',
          background: 'rgba(99, 102, 241, 0.15)',
          color: 'var(--primary)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          margin: '0 auto 12px'
        }}>
          <LogIn size={24} />
        </div>
        <h2 style={{ fontSize: '1.4rem', fontWeight: 700 }}>Welcome Back</h2>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.88rem', marginTop: '4px' }}>
          Authenticate into your ASPMS Auth Service account
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
          <label className="form-label">Username</label>
          <div style={{ position: 'relative' }}>
            <input
              type="text"
              className="form-input"
              placeholder="e.g. testuser"
              value={uname}
              onChange={(e) => setUname(e.target.value)}
              disabled={loading}
              style={{ paddingLeft: '40px' }}
            />
            <User size={16} style={{ position: 'absolute', left: '14px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
          </div>
        </div>

        <div className="form-group">
          <label className="form-label">Password</label>
          <div style={{ position: 'relative' }}>
            <input
              type="password"
              className="form-input"
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              disabled={loading}
              style={{ paddingLeft: '40px' }}
            />
            <Key size={16} style={{ position: 'absolute', left: '14px', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
          </div>
        </div>

        <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: '8px', padding: '12px' }} disabled={loading}>
          {loading ? 'Authenticating...' : (
            <>
              Sign In <ArrowRight size={16} />
            </>
          )}
        </button>
      </form>

      <div style={{ marginTop: '24px', paddingTop: '16px', borderTop: '1px solid var(--border-color)' }}>
        <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)', marginBottom: '8px', display: 'flex', alignItems: 'center', gap: '6px' }}>
          <Sparkles size={12} color="var(--accent-amber)" /> Quick fill test accounts:
        </div>
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
          <button
            type="button"
            className="btn btn-secondary"
            style={{ fontSize: '0.75rem', padding: '4px 10px' }}
            onClick={() => handleQuickFill('admin_demo', 'secret123')}
          >
            admin_demo
          </button>
          <button
            type="button"
            className="btn btn-secondary"
            style={{ fontSize: '0.75rem', padding: '4px 10px' }}
            onClick={() => handleQuickFill('supplier_demo', 'secret123')}
          >
            supplier_demo
          </button>
          <button
            type="button"
            className="btn btn-secondary"
            style={{ fontSize: '0.75rem', padding: '4px 10px' }}
            onClick={() => handleQuickFill('customer_demo', 'secret123')}
          >
            customer_demo
          </button>
        </div>
      </div>

      <div style={{ textAlign: 'center', marginTop: '18px', fontSize: '0.85rem', color: 'var(--text-muted)' }}>
        Don't have an account?{' '}
        <button
          onClick={switchToRegister}
          style={{ background: 'none', border: 'none', color: 'var(--primary)', fontWeight: 600, cursor: 'pointer', textDecoration: 'underline' }}
        >
          Create one now
        </button>
      </div>
    </div>
  );
}
