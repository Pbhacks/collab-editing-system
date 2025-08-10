import React, { useState } from "react";
import api from "../api";
import { useNavigate } from "react-router-dom";

export default function Register() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const nav = useNavigate();

  async function handleRegister() {
    try {
      const payload = { username, password, email };
      const res = await api.post("/users/register", payload);
      alert("Registered: " + res.data.username);
      nav("/");
    } catch (err) {
      alert("Register failed: " + (err?.response?.data || err.message));
    }
  }

  return (
    <div className="card" style={{maxWidth:520, margin:"20px auto"}}>
      <h2>Register</h2>
      <input placeholder="Username" type="text" value={username} onChange={e=>setUsername(e.target.value)} />
      <div style={{height:8}} />
      <input placeholder="Email" type="email" value={email} onChange={e=>setEmail(e.target.value)} />
      <div style={{height:8}} />
      <input placeholder="Password" type="password" value={password} onChange={e=>setPassword(e.target.value)} />
      <div style={{height:8}} />
      <div className="row">
        <button onClick={handleRegister}>Create account</button>
        <button className="secondary" onClick={() => nav("/")}>Back to Login</button>
      </div>
    </div>
  );
}
