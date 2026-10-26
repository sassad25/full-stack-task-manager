import { useState } from 'react';
import { useAppDispatch } from '../store/hooks';
import { createTask } from '../store/tasksSlice';
import type { TaskPriority } from '../types';

export default function TaskForm() {
  const dispatch = useAppDispatch();
  const [title, setTitle] = useState('');
  const [priority, setPriority] = useState<TaskPriority>('MEDIUM');
  const [dueDate, setDueDate] = useState('');

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!title.trim()) return;
    void dispatch(
      createTask({
        title: title.trim(),
        priority,
        dueDate: dueDate || undefined,
      }),
    );
    setTitle('');
    setDueDate('');
    setPriority('MEDIUM');
  };

  return (
    <form className="card task-form" onSubmit={onSubmit}>
      <input
        className="task-form__title"
        placeholder="Add a task…"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
      />
      <select value={priority} onChange={(e) => setPriority(e.target.value as TaskPriority)}>
        <option value="LOW">Low</option>
        <option value="MEDIUM">Medium</option>
        <option value="HIGH">High</option>
      </select>
      <input type="date" value={dueDate} onChange={(e) => setDueDate(e.target.value)} />
      <button type="submit">Add</button>
    </form>
  );
}
