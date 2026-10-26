import type {
  CreateTaskInput,
  LoginResponse,
  Task,
  UpdateTaskInput,
} from './types';

const BASE = '/api/v1';

function authHeaders(token: string): HeadersInit {
  return {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${token}`,
  };
}

async function handle<T>(res: Response): Promise<T> {
  if (!res.ok) {
    let detail = res.statusText;
    try {
      const problem = await res.json();
      detail = problem.detail ?? problem.title ?? detail;
    } catch {
      // response had no JSON body
    }
    throw new Error(detail);
  }
  if (res.status === 204) {
    return undefined as T;
  }
  return (await res.json()) as T;
}

export const api = {
  async login(username: string, password: string): Promise<LoginResponse> {
    const res = await fetch(`${BASE}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password }),
    });
    return handle<LoginResponse>(res);
  },

  async listTasks(token: string): Promise<Task[]> {
    const res = await fetch(`${BASE}/tasks`, { headers: authHeaders(token) });
    return handle<Task[]>(res);
  },

  async createTask(token: string, input: CreateTaskInput): Promise<Task> {
    const res = await fetch(`${BASE}/tasks`, {
      method: 'POST',
      headers: authHeaders(token),
      body: JSON.stringify(input),
    });
    return handle<Task>(res);
  },

  async updateTask(token: string, id: string, input: UpdateTaskInput): Promise<Task> {
    const res = await fetch(`${BASE}/tasks/${id}`, {
      method: 'PUT',
      headers: authHeaders(token),
      body: JSON.stringify(input),
    });
    return handle<Task>(res);
  },

  async deleteTask(token: string, id: string): Promise<void> {
    const res = await fetch(`${BASE}/tasks/${id}`, {
      method: 'DELETE',
      headers: authHeaders(token),
    });
    return handle<void>(res);
  },
};
