import React, { useEffect, useState } from "react";
import api from "../api";
import { useParams } from "react-router-dom";

export default function VersionViewer() {
  const { id } = useParams(); // doc id
  const [versions, setVersions] = useState([]);

  useEffect(() => {
    if (!id) return;
    api.get(`/versions/history/${id}`).then(res => {
      setVersions(res.data || []);
    }).catch(() => {});
  }, [id]);

  async function handleRevert(versionId) {
    if (!confirm("Revert to this version?")) return;
    try {
      const vRes = await api.get(`/versions/revert/${versionId}`);
      const version = vRes.data;
      // now call doc-service to update the doc content
      await api.put(`/docs/edit/${version.docId}`, version.content, { headers: { "Content-Type": "text/plain" }});
      alert("Reverted");
    } catch (err) {
      alert("Revert failed");
    }
  }

  if (!id) return <div className="card">No document selected</div>;
  return (
    <div className="card">
      <h3>Version history for doc {id}</h3>
      <div className="version-list">
        {versions.map(v => (
          <div key={v.id} className="version-item">
            <div style={{display:"flex", justifyContent:"space-between", alignItems:"center"}}>
              <div className="small">{v.time} — {v.editorName}</div>
              <button className="secondary" onClick={() => handleRevert(v.id)}>Revert</button>
            </div>
            <div style={{height:6}} />
            <pre style={{whiteSpace:"pre-wrap"}}>{v.content}</pre>
          </div>
        ))}
      </div>
    </div>
  );
}
