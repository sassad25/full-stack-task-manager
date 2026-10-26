import { useState } from 'react';
import { useAppDispatch, useAppSelector } from '../store/hooks';
import { login } from '../store/authSlice';

export default function Login() {
  const dispatch = useAppDispatch();
  const status = useAppSelector((s) => s.auth.status);
  const error = useAppSelector((s) => s.auth.error);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (username && password) {
      void dispatch(login({ username, password }));
    }
  };

  return (
    <form className="card login" onSubmit={onSubmit}>
      <h2>Sign in</h2>
      <p className="muted">Use any username / password — a demo JWT is issued for you.</p>
      <label>
        Username
        <input value={username} onChange={(e) => setUsername(e.target.value)} autoFocus />
      </label>
      <label>
        Password
        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
      </label>
      {error && <p className="error">{error}</p>}
      <button type="submit" disabled={status === 'loading'}>
        {status === 'loading' ? 'Signing in…' : 'Sign in'}
      </button>
    </form>
  );
}
