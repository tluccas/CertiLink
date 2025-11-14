import { useState, useContext } from "react";
import { AuthContext } from "../../auth/AuthContext.jsx";
import { loginRequest } from "../../api/AuthApi.jsx";
import "./Login.css";
import { useNavigate } from "react-router-dom";
import { Button } from 'react-bootstrap';


export default function Login() {
    const { login } = useContext(AuthContext);
    const navigate = useNavigate();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    async function handleLogin(e) {
        e.preventDefault();

        try {
            const authToken = await loginRequest(email, password);
            login(authToken); 
            alert("Login realizado com sucesso!");
            navigate("/dashboard"); 
        } catch (e) {
            alert("Usuário ou senha incorretos! Tente novamente.");
        }
    }

    return (
        <div className="login-container">
            <form className="login-card" onSubmit={handleLogin}>
                <h2>Login</h2>

                <input 
                    placeholder="Usuário ou E-mail"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />

                <input 
                    type="password"
                    placeholder="Senha"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />

                <Button className="btn-login" type="submit" >Login</Button>
                <p>Não tem uma conta? <a href="/register">Registre-se</a></p>
            </form>
        </div>
    )
}
