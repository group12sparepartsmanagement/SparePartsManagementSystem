import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081/api/auth';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

export const registerUser = async (data) => {
  try {
    const response = await apiClient.post('/register', data);
    return { success: true, data: response.data, status: response.status };
  } catch (error) {
    const errorData = error.response?.data || { message: error.message || 'Registration failed' };
    return {
      success: false,
      error: errorData,
      status: error.response?.status || 500,
    };
  }
};

export const loginUser = async (data) => {
  try {
    const response = await apiClient.post('/login', data);
    return { success: true, data: response.data, status: response.status };
  } catch (error) {
    const errorData = error.response?.data || { message: error.message || 'Login failed' };
    return {
      success: false,
      error: errorData,
      status: error.response?.status || 500,
    };
  }
};

export const ROLE_MAP = {
  1: { id: 1, name: 'Admin', badgeClass: 'badge-admin' },
  2: { id: 2, name: 'Supplier', badgeClass: 'badge-supplier' },
  3: { id: 3, name: 'Customer', badgeClass: 'badge-customer' },
  4: { id: 4, name: 'Staff', badgeClass: 'badge-staff' },
};

export const getRoleBadge = (roleNameOrId) => {
  if (!roleNameOrId) return { name: 'Unknown', badgeClass: 'badge-unknown' };
  
  if (typeof roleNameOrId === 'number') {
    return ROLE_MAP[roleNameOrId] || { name: `Role #${roleNameOrId}`, badgeClass: 'badge-unknown' };
  }
  
  const lower = String(roleNameOrId).toLowerCase();
  if (lower.includes('admin')) return ROLE_MAP[1];
  if (lower.includes('supplier')) return ROLE_MAP[2];
  if (lower.includes('customer')) return ROLE_MAP[3];
  if (lower.includes('staff')) return ROLE_MAP[4];
  
  return { name: roleNameOrId, badgeClass: 'badge-unknown' };
};
