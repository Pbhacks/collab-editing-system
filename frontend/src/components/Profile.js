import React, { useState } from "react";
import api from "../api";

export default function Profile() {
  const stored = JSON.parse(localStorage.getItem("user") || "null");
  const [email, setEmail] = useState(stored?.email || "");
  const [password, setPassword] = useState(stored?.password || "");
  const [status, setStatus] = useState("");

  async function updateProfile() {
    try {
      const payload = { ...stored, email, password };
      const res = await api.put(`/users/${stored.id}`, payload);
      localStorage.setItem("user", JSON.stringify(res.data));
      setStatus("Saved");
    } catch (err) {
      setStatus("Error saving");
    }
  }

  if (!stored) return <div className="card">Please login first.</div>;

  return (
    <div className="card" style={{maxWidth:620}}>
      <h2>Profile</h2>
      <div className="small">Username: {stored.username}</div>
      <div style={{height:8}} />
      <input value={email} onChange={(e)=>setEmail(e.target.value)} />
      <div style={{height:8}} />
      <input type="password" value={password} onChange={(e)=>setPassword(e.target.value)} />
      <div style={{height:10}} />
      <button onClick={updateProfile}>Save</button>
      <div style={{height:8}} className="small">{status}</div>
    </div>
  );
}
