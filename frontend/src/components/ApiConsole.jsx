import React, { useState } from 'react';
import { Terminal, RotateCcw, Copy, Check, Info, Send } from 'lucide-react';
import { registerUser, loginUser } from '../services/authService';

const PRESETS = {
  login: {
    endpoint: '/api/auth/login',
    method: 'POST',
    body: JSON.stringify({ uname: 'testuser', password: 'secret123' }, null, 2),
    description: 'Authenticates username and password against BCrypt hashed records in DB.',
  },
  register: {
    endpoint: '/api/auth/register',
    method: 'POST',
    body: JSON.stringify(
      {
        uname: 'testuser_' + Math.floor(Math.random() * 1000),
        password: 'secret123',
        address: 'Pune, Maharashtra',
        rid: 3,
      },
      null,
      2
    ),
    description: 'Registers new user entity with role ID (1=Admin, 2=Supplier, 3=Customer, 4=Staff).',
  },
};

export default function ApiConsole({ addToast }) {
  const [selectedEndpoint, setSelectedEndpoint] = useState('login');
  const [requestBody, setRequestBody] = useState(PRESETS.login.body);
  const [responseState, setResponseState] = useState(null);
  const [loading, setLoading] = useState(false);
  const [copied, setCopied] = useState(false);

  const handleSelectEndpoint = (key) => {
    setSelectedEndpoint(key);
    setRequestBody(PRESETS[key].body);
    setResponseState(null);
  };

  const handleExecute = async () => {
    let parsedBody;
    try {
      parsedBody = JSON.parse(requestBody);
    } catch (e) {
      addToast('error', 'Invalid JSON payload in request body');
      return;
    }

    setLoading(true);
    setResponseState(null);

    const startTime = performance.now();
    let result;

    if (selectedEndpoint === 'login') {
      result = await loginUser(parsedBody);
    } else {
      result = await registerUser(parsedBody);
    }

    const duration = Math.round(performance.now() - startTime);
    setLoading(false);

    setResponseState({
      status: result.status,
      success: result.success,
      data: result.success ? result.data : result.error,
      duration,
    });

    if (result.success) {
      addToast('success', `API ${PRESETS[selectedEndpoint].endpoint} executed (${result.status} OK)`);
    } else {
      addToast('error', `API ${PRESETS[selectedEndpoint].endpoint} failed (${result.status})`);
    }
  };

  const handleCopy = () => {
    if (!responseState) return;
    navigator.clipboard.writeText(JSON.stringify(responseState.data, null, 2));
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="fade-in" style={{ maxWidth: '1000px', margin: '0 auto', padding: '20px 0' }}>
      
      {/* Console Header */}
      <div style={{ marginBottom: '20px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '12px' }}>
        <div>
          <h2 style={{ fontSize: '1.4rem', fontWeight: 800, display: 'flex', alignItems: 'center', gap: '10px' }}>
            <Terminal size={22} color="var(--primary)" /> API Console & Live Endpoints Tester
          </h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.88rem', marginTop: '4px' }}>
            Directly invoke Spring Boot REST API endpoints for ASPMS Auth Service
          </p>
        </div>

        <button className="btn btn-secondary" onClick={() => setRequestBody(PRESETS[selectedEndpoint].body)}>
          <RotateCcw size={14} /> Reset Body
        </button>
      </div>

      {/* Endpoint Selector Tabs */}
      <div className="tab-list">
        <button
          className={`tab-btn ${selectedEndpoint === 'login' ? 'active' : ''}`}
          onClick={() => handleSelectEndpoint('login')}
        >
          <span style={{ padding: '2px 6px', background: 'rgba(16, 185, 129, 0.2)', color: '#34d399', borderRadius: '4px', fontSize: '0.75rem', fontWeight: 700 }}>
            POST
          </span>
          /api/auth/login
        </button>

        <button
          className={`tab-btn ${selectedEndpoint === 'register' ? 'active' : ''}`}
          onClick={() => handleSelectEndpoint('register')}
        >
          <span style={{ padding: '2px 6px', background: 'rgba(16, 185, 129, 0.2)', color: '#34d399', borderRadius: '4px', fontSize: '0.75rem', fontWeight: 700 }}>
            POST
          </span>
          /api/auth/register
        </button>
      </div>

      <div style={{ background: 'rgba(99, 102, 241, 0.08)', border: '1px solid rgba(99, 102, 241, 0.2)', borderRadius: 'var(--radius-sm)', padding: '12px 16px', marginBottom: '20px', fontSize: '0.85rem', color: 'var(--text-muted)', display: 'flex', alignItems: 'center', gap: '8px' }}>
        <Info size={16} color="var(--primary)" />
        <span>{PRESETS[selectedEndpoint].description}</span>
      </div>

      {/* Request & Response Split Grid */}
      <div className="grid-2">
        
        {/* Request Pane */}
        <div className="glass-card" style={{ padding: '20px', display: 'flex', flexDirection: 'column' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '12px' }}>
            <span style={{ fontSize: '0.9rem', fontWeight: 700, color: 'var(--text-main)' }}>
              Request Body (JSON)
            </span>
            <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)', fontFamily: 'var(--font-mono)' }}>
              Content-Type: application/json
            </span>
          </div>

          <textarea
            className="form-textarea"
            style={{
              fontFamily: 'var(--font-mono)',
              fontSize: '0.88rem',
              height: '240px',
              resize: 'vertical',
              lineHeight: 1.5,
              background: '#060913',
              color: '#93c5fd',
              marginBottom: '16px'
            }}
            value={requestBody}
            onChange={(e) => setRequestBody(e.target.value)}
          />

          <button className="btn btn-primary" onClick={handleExecute} disabled={loading} style={{ width: '100%', padding: '12px' }}>
            {loading ? 'Sending Request...' : (
              <>
                <Send size={16} /> Execute API Request
              </>
            )}
          </button>
        </div>

        {/* Response Pane */}
        <div className="glass-card" style={{ padding: '20px', display: 'flex', flexDirection: 'column' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '12px' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
              <span style={{ fontSize: '0.9rem', fontWeight: 700 }}>Response Payload</span>
              {responseState && (
                <span
                  style={{
                    padding: '2px 8px',
                    borderRadius: '999px',
                    fontSize: '0.75rem',
                    fontWeight: 700,
                    background: responseState.success ? 'rgba(16, 185, 129, 0.2)' : 'rgba(244, 63, 94, 0.2)',
                    color: responseState.success ? '#34d399' : '#fda4af',
                    border: `1px solid ${responseState.success ? 'rgba(16, 185, 129, 0.4)' : 'rgba(244, 63, 94, 0.4)'}`
                  }}
                >
                  {responseState.status} Status ({responseState.duration}ms)
                </span>
              )}
            </div>

            {responseState && (
              <button className="btn btn-secondary" onClick={handleCopy} style={{ padding: '4px 10px', fontSize: '0.75rem' }}>
                {copied ? <Check size={14} className="text-emerald-400" /> : <Copy size={14} />}
                {copied ? 'Copied!' : 'Copy JSON'}
              </button>
            )}
          </div>

          <div style={{ flex: 1, minHeight: '240px' }}>
            {loading ? (
              <div style={{ height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--text-muted)', fontSize: '0.9rem' }}>
                Waiting for backend response...
              </div>
            ) : responseState ? (
              <div className="code-block" style={{ height: '240px', overflowY: 'auto' }}>
                {JSON.stringify(responseState.data, null, 2)}
              </div>
            ) : (
              <div style={{ height: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', color: 'var(--text-dim)', textAlign: 'center', border: '1px dashed var(--border-color)', borderRadius: 'var(--radius-sm)', padding: '20px' }}>
                <Terminal size={32} style={{ marginBottom: '8px', opacity: 0.4 }} />
                <span>Click "Execute API Request" to view raw Spring Boot response data.</span>
              </div>
            )}
          </div>
        </div>

      </div>
    </div>
  );
}
