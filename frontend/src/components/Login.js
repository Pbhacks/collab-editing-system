import React, { useState } from "react";
import api from "../api";
import { useNavigate } from "react-router-dom";



export default function Login({ onLogin }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const nav = useNavigate();

  async function handleLogin() {
    try {
      // Our user-service earlier expects username & password as request body or params.
      // We use POST /api/users/login?username=...&password=... as earlier controller accepted that.
      const res = await api.post(`/users/login?username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`);
      const user = res.data;
      localStorage.setItem("user", JSON.stringify(user));
      if (onLogin) onLogin(user);
      nav("/dashboard");
    } catch (err) {
      alert("Login failed: " + (err?.response?.data || err.message));
    }
  }

  return (
    <div className="card" style={{maxWidth: 520, margin: "20px auto"}}>
      <h2>Login</h2>
      <div className="small">Use username & password created during register</div>
      <div style={{height:10}} />
      <input placeholder="Username" value={username} onChange={(e)=>setUsername(e.target.value)} />
      <div style={{height:8}} />
      <input placeholder="Password" type="password" value={password} onChange={(e)=>setPassword(e.target.value)} />
      <div style={{height:8}} />
      <div className="row">
        <button onClick={handleLogin}>Login</button>
        <button className="secondary" onClick={() => nav("/register")}>Register</button>
      </div>
    </div>
  );
}
