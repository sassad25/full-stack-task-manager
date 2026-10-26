import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { api } from '../api';
import type { CreateTaskInput, Task, UpdateTaskInput } from '../types';
import type { RootState } from './index';

interface TasksState {
  items: Task[];
  status: 'idle' | 'loading' | 'failed';
  error: string | null;
}

const initialState: TasksState = {
  items: [],
  status: 'idle',
  error: null,
};

function requireToken(state: RootState): string {
  const token = state.auth.token;
  if (!token) {
    throw new Error('Not authenticated');
  }
  return token;
}

export const fetchTasks = createAsyncThunk<Task[], void, { state: RootState }>(
  'tasks/fetch',
  async (_, { getState }) => api.listTasks(requireToken(getState())),
);

export const createTask = createAsyncThunk<Task, CreateTaskInput, { state: RootState }>(
  'tasks/create',
  async (input, { getState }) => api.createTask(requireToken(getState()), input),
);

export const updateTask = createAsyncThunk<Task, { id: string; input: UpdateTaskInput }, { state: RootState }>(
  'tasks/update',
  async ({ id, input }, { getState }) => api.updateTask(requireToken(getState()), id, input),
);

export const deleteTask = createAsyncThunk<string, string, { state: RootState }>(
  'tasks/delete',
  async (id, { getState }) => {
    await api.deleteTask(requireToken(getState()), id);
    return id;
  },
);

const tasksSlice = createSlice({
  name: 'tasks',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchTasks.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchTasks.fulfilled, (state, action) => {
        state.status = 'idle';
        state.items = action.payload;
      })
      .addCase(fetchTasks.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.error.message ?? 'Failed to load tasks';
      })
      .addCase(createTask.fulfilled, (state, action) => {
        state.items.unshift(action.payload);
      })
      .addCase(updateTask.fulfilled, (state, action) => {
        const idx = state.items.findIndex((t) => t.id === action.payload.id);
        if (idx >= 0) {
          state.items[idx] = action.payload;
        }
      })
      .addCase(deleteTask.fulfilled, (state, action) => {
        state.items = state.items.filter((t) => t.id !== action.payload);
      });
  },
});

export default tasksSlice.reducer;
