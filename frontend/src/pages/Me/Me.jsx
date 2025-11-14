import { useContext, useEffect, useState } from "react";
import { AuthContext } from "../../auth/AuthContext.jsx";
import { apiClient } from "../../api/ApiClient.jsx";
import "./Me.css";
import 'bootstrap-icons/font/bootstrap-icons.css';

export default function Profile() {
    const { token, logout } = useContext(AuthContext);
    const [user, setUser] = useState(null);

    useEffect(() => {
        if (!token) return;
        apiClient(token)("/users/me")
            .then(r => {
                if (r.status === 401) { logout(); throw new Error("Expirado"); }
                return r.json();
            })
            .then(setUser)
            .catch(console.error);
    }, [token]);

    if (!token) return null;
    if (!user) return <p>Carregando...</p>;

    function getRole(role){
        if(role === "ROLE_PROFESSOR"){
            return "Professor";
        }else{
            return "Aluno";
        }
    }

    return (
        <div className="bg-light">
            <div className="container d-flex justify-content-center align-items-center" style={{ minHeight: "100vh" }}>
                <div className="card profile-card shadow mt-4">
                    <div className="card-body text-center">
                        <img
                            src="https://i.pinimg.com/736x/2c/47/d5/2c47d5dd5b532f83bb55c4cd6f5bd1ef.jpg"
                            alt="User Profile"
                            className="rounded-circle profile-img mb-3"
                        />
                        <h3 className="card-title mb-0">{user.name}</h3>
                        <p className="text-muted">{getRole(user.role)}</p>


                        <ul className="list-group list-group-flush mb-3">
                            <li className="list-group-item bg-transparent">
                                <i className="bi bi-envelope-fill me-2"></i>{user.email}
                            </li>
                            <li className="list-group-item bg-transparent"><i className="bi bi-briefcase-fill me-2"></i>CertLink</li>
                        </ul>

                        <div className="d-flex justify-content-center text-center mt-4">
                            <div>
                                <h5 className="mb-0">1</h5>
                                <small className="text-muted">Certificados Gerados</small>

                                 <button className="btn btn-primary w-100 mt-4" onClick={logout}>Logout</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );

}