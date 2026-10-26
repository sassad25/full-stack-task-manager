export type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'DONE';
export type TaskPriority = 'LOW' | 'MEDIUM' | 'HIGH';

export interface Task {
  id: string;
  title: string;
  description?: string | null;
  status: TaskStatus;
  priority: TaskPriority;
  dueDate?: string | null;
  version: number;
  createdAt: string;
}

export interface CreateTaskInput {
  title: string;
  description?: string;
  priority: TaskPriority;
  dueDate?: string;
}

export interface UpdateTaskInput {
  title: string;
  description?: string;
  status: TaskStatus;
  priority: TaskPriority;
  dueDate?: string;
}

export interface LoginResponse {
  token: string;
  tokenType: string;
  expiresInSeconds: number;
  username: string;
}
