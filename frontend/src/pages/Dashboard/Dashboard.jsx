import { useContext, useEffect, useState } from "react";
import { AuthContext } from "../../auth/AuthContext.jsx";
import { apiClient } from "../../api/ApiClient.jsx";
import "./Dashboard.css";

export default function Dashboard() {
  const { token, logout } = useContext(AuthContext);
  const [loading, setLoading] = useState(true);
  const [templates, setTemplates] = useState([]);

  useEffect(() => {
   
    if (!token) {
      setLoading(false);
      return;
    }

    apiClient(token)("/templates/all")
      .then((r) => {
        if (!r.ok) throw new Error("Falha ao carregar templates");
        return r.json();
      })
      .then((data) => {
        console.log("Templates:", data);
        setTemplates(data);
      })
      .catch((err) => console.error("Erro fetch templates:", err))
      .finally(() => setLoading(false));
  }, [token]);

  if (!token) return null;
  if (loading) return <p>Carregando...</p>;

  return (
    <div className="dashboard-container">
      <h1>Dashboard</h1>
      {/* Exibição do token para testes */}
      <div style={{
        background: '#222',
        color: '#fff',
        padding: '0.75rem',
        borderRadius: '6px',
        fontFamily: 'monospace',
        wordBreak: 'break-all',
        marginBottom: '1rem'
      }}>
        <strong>Token JWT:</strong> {token}
      </div>
      <button onClick={logout}>Logout</button>
      {templates && templates.length > 0 ? (
        <ul>
          {templates.map((t) => (
            <li key={t.id || t.name}>{t.name || JSON.stringify(t)}</li>
          ))}
        </ul>
      ) : (
        <p>Nenhum template encontrado.</p>
      )}
    </div>
  );
}
