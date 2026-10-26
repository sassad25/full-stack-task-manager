import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { api } from '../api';

interface AuthState {
  token: string | null;
  username: string | null;
  status: 'idle' | 'loading' | 'failed';
  error: string | null;
}

const persistedToken = localStorage.getItem('tm_token');
const persistedUser = localStorage.getItem('tm_user');

const initialState: AuthState = {
  token: persistedToken,
  username: persistedUser,
  status: 'idle',
  error: null,
};

export const login = createAsyncThunk(
  'auth/login',
  async (credentials: { username: string; password: string }) => {
    const res = await api.login(credentials.username, credentials.password);
    return res;
  },
);

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    logout(state) {
      state.token = null;
      state.username = null;
      localStorage.removeItem('tm_token');
      localStorage.removeItem('tm_user');
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(login.pending, (state) => {
        state.status = 'loading';
        state.error = null;
      })
      .addCase(login.fulfilled, (state, action) => {
        state.status = 'idle';
        state.token = action.payload.token;
        state.username = action.payload.username;
        localStorage.setItem('tm_token', action.payload.token);
        localStorage.setItem('tm_user', action.payload.username);
      })
      .addCase(login.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message ?? 'Login failed';
      });
  },
});

export const { logout } = authSlice.actions;
export default authSlice.reducer;
