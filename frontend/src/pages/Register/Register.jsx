import { useState } from "react";
import { registerRequest } from "../../api/AuthApi.jsx";
import "./Register.css";
import { useNavigate } from "react-router-dom";
import { Button, FormSelect } from 'react-bootstrap';


export default function Register() {
    const navigate = useNavigate();

    const [ name, setName ] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [ role, setRole ] = useState("");


    async function handleRegister(e) {
        e.preventDefault();

        const roleObj = role === "ROLE_PROFESSOR"
        ? { id: 1, name: "ROLE_PROFESSOR" }
        : role === "ROLE_ALUNO"
        ? { id: 2, name: "ROLE_ALUNO" }
        : null;

         if (!roleObj) {
            alert("Selecione um cargo válido!");
            return;
        }

        try {
            await registerRequest(name, email, password, roleObj);
            alert("Usuario criado com sucesso!");
            navigate("/login"); 
        } catch (e) {
            alert("Erro ao criar usuario! Tente novamente.");
        }
    }

    return (
        <div className="register-container">
            <form className="register-card" onSubmit={handleRegister}>
                <h2>Registrar-se</h2>

                <input 
                    placeholder="E-mail"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />

                <input 
                    placeholder="Nome"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />

                <input 
                    type="password"
                    placeholder="Senha"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />

                <FormSelect
                    value={role}
                    onChange={(e) => setRole(e.target.value)}
                >
                    <option>Cargo</option>
                    <option value="ROLE_PROFESSOR">Professor</option>
                    <option value="ROLE_ALUNO">Aluno</option>
                </FormSelect>

                <Button type="submit" >Registrar</Button>
            </form>
        </div>
    )
}
