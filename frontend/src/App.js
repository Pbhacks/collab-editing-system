// src/App.js
import React, { useEffect, useState } from "react";
import { BrowserRouter, Routes, Route, useNavigate } from "react-router-dom";
import Login from "./components/Login";
import Register from "./components/Register";
import Profile from "./components/Profile";
import Editor from "./components/Editor";
import VersionViewer from "./components/VersionViewer";

function Navbar({ onLogout, theme, toggleTheme }) {
  const user = JSON.parse(localStorage.getItem("user") || "null");
  const navigate = useNavigate();

  return (
    <div className="navbar">
      <div className="brand"><span className="logo" /> Collab Editor</div>
      <div className="nav-actions">
        <button onClick={toggleTheme} title="Toggle theme">
          {theme === "dark" ? "🌙" : "☀️"}
        </button>
        {user ? (
          <>
            <span className="small">Hi, {user.username}</span>
            <button onClick={() => navigate("/dashboard")}>Dashboard</button>
            <button
              className="secondary"
              onClick={() => {
                localStorage.removeItem("user");
                onLogout();
                navigate("/");
              }}
            >
              Logout
            </button>
          </>
        ) : (
          <>
            <button onClick={() => navigate("/")}>Login</button>
            <button
              className="secondary"
              onClick={() => navigate("/register")}
            >
              Register
            </button>
          </>
        )}
      </div>
    </div>
  );
}

function AppShell({ children, onLogout, theme, toggleTheme }) {
  return (
    <div className="container">
      <div style={{ height: 12 }} />
      <Navbar onLogout={onLogout} theme={theme} toggleTheme={toggleTheme} />
      <div style={{ height: 12 }} />
      {children}
    </div>
  );
}

export default function App() {
  const [theme, setTheme] = useState(localStorage.getItem("theme") || "light");
  const [isLoggedIn, setIsLoggedIn] = useState(
    !!localStorage.getItem("user")
  );

  useEffect(() => {
    document.body.classList.toggle("dark", theme === "dark");
    localStorage.setItem("theme", theme);
  }, [theme]);

  const handleLogout = () => {
    setIsLoggedIn(false);
  };

  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/"
          element={<Login onLogin={() => setIsLoggedIn(true)} />}
        />
        <Route path="/register" element={<Register />} />
        <Route
          path="/profile"
          element={
            <AppShell
              onLogout={handleLogout}
              theme={theme}
              toggleTheme={() =>
                setTheme((t) => (t === "dark" ? "light" : "dark"))
              }
            >
              <Profile />
            </AppShell>
          }
        />
        <Route
          path="/dashboard"
          element={
            <AppShell
              onLogout={handleLogout}
              theme={theme}
              toggleTheme={() =>
                setTheme((t) => (t === "dark" ? "light" : "dark"))
              }
            >
              <Editor />
            </AppShell>
          }
        />
        <Route
          path="/editor/:id"
          element={
            <AppShell
              onLogout={handleLogout}
              theme={theme}
              toggleTheme={() =>
                setTheme((t) => (t === "dark" ? "light" : "dark"))
              }
            >
              <Editor />
            </AppShell>
          }
        />
        <Route
          path="/versions/:id"
          element={
            <AppShell
              onLogout={handleLogout}
              theme={theme}
              toggleTheme={() =>
                setTheme((t) => (t === "dark" ? "light" : "dark"))
              }
            >
              <VersionViewer />
            </AppShell>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}
