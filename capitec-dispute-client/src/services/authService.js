import apiClient from './apiClient';

export const authService = {
  login: async (username, password) => {
    try {
      const response = await apiClient.post('/auth/login', {
        username,
        password
      });

      const authData = response.data;

      const token = authData.accessToken || authData.token;
      if (token) {
        localStorage.setItem('authToken', token);
      }

      if (authData.refreshToken) {
        localStorage.setItem('refreshToken', authData.refreshToken);
      }

      const userInfo = {
        username: authData.username,
        email: authData.email,
        firstName: authData.firstName,
        lastName: authData.lastName,
        roles: authData.roles
      };

      localStorage.setItem('userInfo', JSON.stringify(userInfo));

      return response.data;
    } catch (error) {
      console.error('Login failed:', error);
      throw new Error(error.response?.data?.message || error.response?.data?.error || 'Login failed. Please try again.');
    }
  },

  logout: () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('userInfo');
  },

  isAuthenticated: () => {
    return !!localStorage.getItem('authToken');
  },

  getUserInfo: () => {
    const userInfo = localStorage.getItem('userInfo');
    return userInfo ? JSON.parse(userInfo) : null;
  },

};
