import React, { useState } from "react";
import { BrowserRouter, Routes, Route, useNavigate } from "react-router-dom";
import Login from "./components/Login";
import Register from "./components/Register";
import Profile from "./components/Profile";
import Editor from "./components/Editor";
import VersionViewer from "./components/VersionViewer";

/* Small Navbar */
function Navbar({ onLogout }) {
  const user = JSON.parse(localStorage.getItem("user") || "null");
  const navigate = useNavigate();
  return (
    <div className="navbar">
      <div className="brand">Collab Editor</div>
      <div className="nav-actions">
        {user ? (
          <>
            <span className="small">Hi, {user.username}</span>
            <button onClick={() => navigate("/dashboard")}>Dashboard</button>
            <button onClick={() => { localStorage.removeItem("user"); onLogout(); navigate("/"); }} className="secondary">Logout</button>
          </>
        ) : (
          <>
            <button onClick={() => navigate("/")}>Login</button>
            <button onClick={() => navigate("/register")} className="secondary">Register</button>
          </>
        )}
      </div>
    </div>
  );
}

/* Simple wrapper to provide navbar on all pages */
function AppShell({ children, onLogout }) {
  return (
    <div className="container">
      <div style={{ height: 12 }} />
      <Navbar onLogout={onLogout} />
      <div style={{ height: 12 }} />
      {children}
    </div>
  );
}

export default function App() {
  const [tick, setTick] = useState(0); // used to force re-render on logout
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login onLogin={() => setTick(t => t+1)} />} />
        <Route path="/register" element={<Register />} />
        <Route path="/profile" element={<AppShell><Profile /></AppShell>} />
        <Route path="/dashboard" element={<AppShell><Editor /></AppShell>} />
        <Route path="/editor/:id" element={<AppShell><Editor /></AppShell>} />
        <Route path="/versions/:id" element={<AppShell><VersionViewer /></AppShell>} />
      </Routes>
    </BrowserRouter>
  );
}
