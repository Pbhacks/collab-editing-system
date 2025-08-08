// src/components/Editor.js
import React, { useEffect, useState, useRef } from "react";
import api from "../api";
import { connectWebSocket, sendEditViaWS } from "../websocket";
import { useNavigate, useParams } from "react-router-dom";

export default function Editor(){
  const { id } = useParams();
  const [docs, setDocs] = useState([]);
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [status, setStatus] = useState("");
  const [wsConnected, setWsConnected] = useState(false);
  const pollRef = useRef(null);
  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem("user") || "null");

  useEffect(()=> { fetchDocs(); }, []);

  useEffect(()=> {
    if (!id) { if (pollRef.current) clearInterval(pollRef.current); return; }
    // fetch the doc by id (title + content)
    api.get(`/docs/${id}`).then(res => {
      setTitle(res.data.title || "");
      setContent(res.data.content || "");
    }).catch(()=>{});

    // try websocket
    connectWebSocket(id, (message)=>{
      if (message.content !== undefined) setContent(message.content);
      if (message.title !== undefined) setTitle(message.title);
    }).then(({connected, disconnect})=>{
      setWsConnected(connected);
      if (!connected) {
        pollRef.current = setInterval(()=> {
          api.get(`/docs/${id}`).then(r=>{
            if (r.data) {
              setContent(r.data.content || "");
              setTitle(r.data.title || "");
            }
          }).catch(()=>{});
        }, 3000);
      } else if (pollRef.current) {
        clearInterval(pollRef.current);
      }
    });

    return ()=> {
      if (pollRef.current) clearInterval(pollRef.current);
    };
  }, [id]);

  async function fetchDocs(){
    try {
      const res = await api.get(`/docs/user/${user?.id || 0}`);
      setDocs(res.data || []);
    } catch (e) { console.error(e); }
  }

  async function createDoc(){
    try {
      const payload = { title: title || "Untitled", content: content || "", ownerId: user?.id || null };
      const res = await api.post("/docs/create", payload);
      navigate(`/editor/${res.data.id}`);
      fetchDocs();
    } catch (err) {
      alert("Create failed");
    }
  }

  async function handleChange(e){
    const val = e.target.value;
    setContent(val);
    setStatus("Editing...");
    if (!id) return;
    const wsOk = sendEditViaWS(id, { content: val, editorName: user?.username });
    if (!wsOk) {
      // fallback: send via REST
      try {
        await api.put(`/docs/edit/${id}`, val, { headers: { "Content-Type": "text/plain" }});
      } catch (e) {}
    }
  }

  async function saveVersion(){
    if (!id) return alert("Open a document first");
    try {
      await api.post("/versions/save", { docId: parseInt(id,10), content, editorName: user?.username });
      setStatus("Version saved");
    } catch (e) {
      setStatus("Version save failed");
    }
  }

  if (!id) {
    return (
      <div>
        <h2 className="card">Dashboard</h2>
        <div className="card">
          <h3>Create New Document</h3>
          <input placeholder="Title" type="text" value={title} onChange={e=>setTitle(e.target.value)} />
          <div style={{height:8}}/>
          <textarea placeholder="Start writing..." value={content} onChange={e=>setContent(e.target.value)} />
          <div style={{height:8}}/>
          <div className="row">
            <button onClick={createDoc}>Create & Open</button>
            <button className="secondary" onClick={fetchDocs}>Refresh List</button>
          </div>
        </div>

        <div className="card">
          <h3>Your Documents</h3>
          <ul className="doc-list">
            {docs.length===0 && <li className="small">No documents yet.</li>}
            {docs.map(d=>(
              <li key={d.id} onClick={()=>navigate(`/editor/${d.id}`)}>
                <div>
                  <strong>{d.title || "Untitled"}</strong>
                  <div className="small">id: {d.id}</div>
                </div>
                <div className="small">{d.content ? (d.content.slice(0,40)+"...") : "Empty"}</div>
              </li>
            ))}
          </ul>
        </div>
      </div>
    );
  }

  return (
    <div className="editor card">
      <div className="editor-main">
        <div className="editor-meta">
          <div>
            <h3>{title || "Untitled"}</h3>
            <div className="small">doc id: {id} • {wsConnected ? "Realtime" : "Polling"}</div>
          </div>
          <div>
            <button onClick={saveVersion}>Save Version</button>
            <button className="secondary" onClick={()=>navigate(`/versions/${id}`)}>View Versions</button>
          </div>
        </div>

        <div style={{height:10}}/>
        <textarea className="editor-input" value={content} onChange={handleChange} />
        <div style={{height:8}} className="small">{status}</div>
      </div>

      <div>
        <div className="card">
          <strong>Quick Info</strong>
          <div className="small">Editing as: {user?.username || "guest"}</div>
          <div style={{height:10}}/>
          <button onClick={()=>api.get(`/docs/${id}`).then(r=>{ setContent(r.data.content || ""); setTitle(r.data.title || ""); })}>Refresh</button>
        </div>

        <div className="card version-list">
          <h4>Recent versions</h4>
          <Versions docId={id} />
        </div>
      </div>
    </div>
  );
}

function Versions({ docId }){
  const [versions, setVersions] = useState([]);
  useEffect(()=> {
    let mounted=true;
    api.get(`/versions/history/${docId}`).then(r=> { if(mounted) setVersions(r.data || []); }).catch(()=>{});
    return ()=> mounted=false;
  }, [docId]);

  return (
    <div>
      {versions.length===0 && <div className="small">No versions yet.</div>}
      {versions.map(v => (
        <div className="version-item" key={v.id}>
          <div style={{display:"flex",justifyContent:"space-between",alignItems:"center"}}>
            <div className="small">{v.time} — {v.editorName}</div>
          </div>
          <div style={{height:6}} />
          <pre style={{whiteSpace:"pre-wrap",margin:0}}>{v.content}</pre>
        </div>
      ))}
    </div>
  );
}
