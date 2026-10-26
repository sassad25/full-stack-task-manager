# Full-Stack Task Manager

A full-stack task management application: a **React + Redux Toolkit + TypeScript** frontend (Vite)
talking to a **JWT-secured Spring Boot** REST API. Demonstrates end-to-end full-stack delivery —
typed state management, a clean REST API, stateless JWT auth, per-user data isolation, and tests on
both sides.

> Author: **Muhammad Shehzeb Assad** — Senior Full Stack Java Developer
> Portfolio: https://github.com/sassad25

---

## Stack

**Frontend** — React 18, Redux Toolkit (`createSlice` + `createAsyncThunk`), TypeScript (strict),
Vite, Cypress (E2E).
**Backend** — Java 17, Spring Boot 3.3, Spring Security (OAuth2 resource server), Spring Data JPA,
JJWT (HS256), H2/PostgreSQL, JUnit 5 + MockMvc.

## Highlights

- **Typed, predictable state** — Redux Toolkit slices with async thunks; fully typed `useAppSelector`
  / `useAppDispatch` hooks and typed thunk state.
- **Stateless JWT auth** — login issues an HS256 JWT; the SPA stores it and sends it as a bearer
  token. The API validates it as an OAuth2 resource server.
- **Per-user isolation** — the owner id comes from the JWT subject (never the client), so users only
  ever see and mutate their own tasks (enforced in the service layer and covered by a test).
- **Clean API contract** — RFC 7807 problem responses, Bean Validation on inputs, optimistic locking
  on the entity.
- **Tested both sides** — backend MockMvc integration tests (CRUD, auth, validation, isolation) and
  a Cypress E2E spec driving the real UI flow.

## Layout

```
full-stack-task-manager/
├── backend/    # Spring Boot API (Java 17)
│   └── src/main/java/io/shehzeb/taskmanager/{domain,service,api,security}
├── frontend/   # React + Redux Toolkit + TypeScript (Vite)
│   └── src/{store,components,api.ts,types.ts}
└── .github/workflows/ci.yml   # builds & tests both halves
```

## Running locally

**Backend** (port 8085):

```bash
cd backend
mvn spring-boot:run
```

**Frontend** (port 5173, proxies /api to the backend):

```bash
cd frontend
npm install
npm run dev
# open http://localhost:5173 — sign in with any username/password
```

## Testing

```bash
# Backend — JUnit + MockMvc integration tests
cd backend && mvn clean verify

# Frontend — type-check + production build
cd frontend && npm run build

# Frontend — Cypress E2E (requires dev server + backend running)
cd frontend && npm run e2e
```

## API

| Method | Path                      | Description                         |
|--------|---------------------------|-------------------------------------|
| POST   | `/api/v1/auth/login`      | Issue a JWT (demo login)            |
| GET    | `/api/v1/tasks`           | List the caller's tasks             |
| POST   | `/api/v1/tasks`           | Create a task                       |
| GET    | `/api/v1/tasks/{id}`      | Get one of the caller's tasks       |
| PUT    | `/api/v1/tasks/{id}`      | Update a task                       |
| DELETE | `/api/v1/tasks/{id}`      | Delete a task                       |

> The login endpoint is intentionally minimal so the stack is self-contained. In production it would
> integrate with an identity provider or the companion
> [secure-auth-service](https://github.com/sassad25/secure-auth-service).

## License

MIT © Muhammad Shehzeb Assad
