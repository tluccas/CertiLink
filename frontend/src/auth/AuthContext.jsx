import { createContext, useState } from "react";

export const AuthContext = createContext();

export function AuthProvider({ children}) {
    const [token, setToken] = useState(localStorage.getItem("authToken"));

    const login = (jwt) => {
        localStorage.setItem("authToken", jwt);
        setToken(jwt);
    }

    const logout = () => {
        localStorage.removeItem("authToken");
        setToken(null);
    }

    return (
        <AuthContext.Provider value= {{ token, login, logout}}>
            {children}
        </AuthContext.Provider>
    )
}