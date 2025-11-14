import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login/Login";
import Register from "./pages/Register/Register";
import Dashboard from "./pages/Dashboard/Dashboard";
import ProtectedRoute from "./auth/ProtectedRoute.jsx";
import Me from "./pages/Me/Me.jsx";


function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
        <Route path="/me" element={<ProtectedRoute><Me /></ProtectedRoute>} />
      </Routes>
    </BrowserRouter>
  )
}

export default App;
