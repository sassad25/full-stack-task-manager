import { useEffect } from 'react';
import { useAppDispatch, useAppSelector } from '../store/hooks';
import { fetchTasks, updateTask, deleteTask } from '../store/tasksSlice';
import { logout } from '../store/authSlice';
import TaskForm from './TaskForm';
import type { Task, TaskStatus } from '../types';

const NEXT_STATUS: Record<TaskStatus, TaskStatus> = {
  TODO: 'IN_PROGRESS',
  IN_PROGRESS: 'DONE',
  DONE: 'TODO',
};

function TaskItem({ task }: { task: Task }) {
  const dispatch = useAppDispatch();

  const advance = () =>
    void dispatch(
      updateTask({
        id: task.id,
        input: {
          title: task.title,
          description: task.description ?? undefined,
          status: NEXT_STATUS[task.status],
          priority: task.priority,
          dueDate: task.dueDate ?? undefined,
        },
      }),
    );

  return (
    <li className={`task task--${task.status.toLowerCase()}`}>
      <div className="task__main">
        <span className={`badge badge--${task.priority.toLowerCase()}`}>{task.priority}</span>
        <span className="task__title">{task.title}</span>
        {task.dueDate && <span className="task__due">due {task.dueDate}</span>}
      </div>
      <div className="task__actions">
        <button onClick={advance}>{task.status}</button>
        <button className="danger" onClick={() => void dispatch(deleteTask(task.id))}>
          Delete
        </button>
      </div>
    </li>
  );
}

export default function TaskBoard() {
  const dispatch = useAppDispatch();
  const { items, status, error } = useAppSelector((s) => s.tasks);
  const username = useAppSelector((s) => s.auth.username);

  useEffect(() => {
    void dispatch(fetchTasks());
  }, [dispatch]);

  return (
    <div className="board">
      <div className="board__bar">
        <span>
          Signed in as <strong>{username}</strong>
        </span>
        <button className="link" onClick={() => dispatch(logout())}>
          Sign out
        </button>
      </div>

      <TaskForm />

      {status === 'loading' && <p className="muted">Loading tasks…</p>}
      {error && <p className="error">{error}</p>}
      {status !== 'loading' && items.length === 0 && <p className="muted">No tasks yet. Add one above.</p>}

      <ul className="task-list">
        {items.map((task) => (
          <TaskItem key={task.id} task={task} />
        ))}
      </ul>
    </div>
  );
}
