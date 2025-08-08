import React, { useEffect, useState, useRef } from "react";
import api from "../api";
import { connectWebSocket, sendEditViaWS } from "../websocket";
import { useNavigate, useParams } from "react-router-dom";

/**
 * Editor component:
 * - If path is /editor/:id -> open that document
 * - If path is /dashboard (no id) -> show list + create new doc UI
 */
export default function Editor() {
  const { id } = useParams(); // doc id if present
  const [docs, setDocs] = useState([]);
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [status, setStatus] = useState("");
  const [wsConnected, setWsConnected] = useState(false);
  const pollRef = useRef(null);
  const nav = useNavigate();
  const user = JSON.parse(localStorage.getItem("user") || "null");

  useEffect(() => {
    fetchDocs();
    // cleanup polling on unmount
    return () => {
      if (pollRef.current) clearInterval(pollRef.current);
    };
  }, []);

  // If an id is provided, open editor mode and try WebSocket
  useEffect(() => {
    if (id) {
      loadDocument(id);
      // try websocket
      connectWebSocket(id, (message) => {
        // message expected { content, title, editorName, docId }
        if (message.content !== undefined) setContent(message.content);
        if (message.title !== undefined) setTitle(message.title);
      }).then(({ connected, disconnect }) => {
        setWsConnected(connected);
        if (!connected) {
          // fallback to polling every 3s
          pollRef.current = setInterval(() => loadDocument(id), 3000);
        } else {
          // when connected, ensure we clear polling
          if (pollRef.current) clearInterval(pollRef.current);
        }
      });
    } else {
      // not editor path
      if (pollRef.current) clearInterval(pollRef.current);
    }
    // eslint-disable-next-line
  }, [id]);

  async function fetchDocs() {
    try {
      const res = await api.get(`/docs/user/${user?.id || 0}`);
      setDocs(res.data || []);
    } catch (err) {
      console.error(err);
    }
  }

  async function createDoc() {
    try {
      const payload = { title: title || "Untitled", content, ownerId: user.id };
      const res = await api.post("/docs/create", payload);
      alert("Created doc id " + res.data.id);
      fetchDocs();
      nav(`/editor/${res.data.id}`);
    } catch (err) {
      alert("Create failed");
    }
  }

  async function loadDocument(docId) {
    try {
      const res = await api.get(`/docs/${docId}`);
      if (res.data) {
        setTitle(res.data.title || "");
        setContent(res.data.content || "");
      }
    } catch (err) {
      console.error("loadDocument", err);
    }
  }

  // Called on every change in editor. We try to send via WS; if not connected use REST
  async function handleContentChange(e) {
    const newContent = e.target.value;
    setContent(newContent);
    setStatus("Editing...");

    if (id) {
      const wsOk = sendEditViaWS(id, { content: newContent, editorName: user.username });
      if (!wsOk) {
        // fallback to REST patch
        try {
          await api.put(`/docs/edit/${id}`, newContent, { headers: { "Content-Type": "text/plain" }});
        } catch (err) { /* ignore for now */ }
      }
    }
  }

  async function saveVersion() {
    if (!id) return alert("Open a saved document to save a version.");
    const payload = { docId: parseInt(id,10), editorName: user.username, content };
    try {
      await api.post("/versions/save", payload);
      setStatus("Version saved");
    } catch (err) {
      setStatus("Version save failed");
    }
  }

  // UI: either list (dashboard) or editor page
  if (!id) {
    return (
      <div>
        <h2 className="card">Dashboard</h2>
        <div className="card">
          <h3>Create New Document</h3>
          <input placeholder="Title" value={title} onChange={e=>setTitle(e.target.value)} />
          <div style={{height:8}} />
          <textarea className="editor-input" placeholder="Start writing..." value={content} onChange={e=>setContent(e.target.value)} />
          <div style={{height:8}} />
          <div className="row">
            <button onClick={createDoc}>Create & Open</button>
            <button className="secondary" onClick={fetchDocs}>Refresh List</button>
          </div>
        </div>

        <div className="card">
          <h3>Your Documents</h3>
          <ul className="doc-list">
            {docs.length===0 && <li className="small">No documents yet.</li>}
            {docs.map(d => (
              <li key={d.id} onClick={()=>nav(`/editor/${d.id}`)}>
                <strong>{d.title || "Untitled"}</strong>
                <div className="small">id: {d.id}</div>
              </li>
            ))}
          </ul>
        </div>
      </div>
    );
  }

  // Editor view for a specific doc id
  return (
    <div className="editor card">
      <div className="editor-main">
        <div style={{display:"flex", justifyContent:"space-between", alignItems:"center"}}>
          <div>
            <h3>{title || "Untitled"}</h3>
            <div className="small">doc id: {id} • {wsConnected ? "Realtime (WS)" : "Polling"}</div>
          </div>
          <div>
            <button onClick={saveVersion}>Save Version</button>
            <button className="secondary" onClick={()=>nav(`/versions/${id}`)}>View Versions</button>
          </div>
        </div>
        <div style={{height:10}} />
        <textarea className="editor-input" value={content} onChange={handleContentChange} />
        <div style={{height:8}} className="small">{status}</div>
      </div>

      <div>
        <div className="card">
          <strong>Quick Info</strong>
          <div className="small">You are editing as: {user?.username || "guest"}</div>
          <div style={{height:10}} />
          <button onClick={()=>loadDocument(id)}>Refresh</button>
        </div>
        <div className="card version-list">
          <h4>Recent versions</h4>
          <VersionList docId={id} />
        </div>
      </div>
    </div>
  );
}

/* Inline small component to show versions (calls version-service) */
function VersionList({ docId }) {
  const [versions, setVersions] = useState([]);
  useEffect(() => {
    let mounted = true;
    api.get(`/versions/history/${docId}`).then(r=> { if(mounted) setVersions(r.data || []); }).catch(()=>{});
    return ()=> mounted=false;
  }, [docId]);
  return (
    <div>
      {versions.map(v => (
        <div key={v.id} className="version-item">
          <div className="small">{v.time} — {v.editorName}</div>
          <div style={{height:6}} />
          <div style={{maxHeight:120, overflow:"auto", background:"#fafafa", padding:8, borderRadius:6}}>
            <pre style={{margin:0, whiteSpace:"pre-wrap"}}>{v.content}</pre>
          </div>
        </div>
      ))}
      {versions.length===0 && <div className="small">No versions yet.</div>}
    </div>
  );
}
