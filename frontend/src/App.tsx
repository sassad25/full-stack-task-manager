import { useAppSelector } from './store/hooks';
import Login from './components/Login';
import TaskBoard from './components/TaskBoard';

export default function App() {
  const token = useAppSelector((state) => state.auth.token);
  return (
    <div className="app">
      <header className="app__header">
        <h1>Task Manager</h1>
        <span className="app__sub">React · Redux Toolkit · TypeScript · Spring Boot</span>
      </header>
      <main className="app__main">{token ? <TaskBoard /> : <Login />}</main>
    </div>
  );
}
