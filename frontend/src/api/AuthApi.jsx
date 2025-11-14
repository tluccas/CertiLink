export async function loginRequest(email, password) {
  const response = await fetch("http://localhost:8080/auth", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password })
  });

  if (!response.ok) {
    throw new Error("Login inválido!");
  }

  let data;
  try {
    data = await response.json();
  } catch (e) {
    throw new Error("Resposta inesperada do servidor.");
  }

  console.log("Login response:", data); // Diagnóstico

  // Tenta várias chaves comuns de token
  const token = data.authToken || data.token || data.jwt;
  if (!token) {
    throw new Error("Token não encontrado na resposta.");
  }
  return token;
}

export async function registerRequest(name, email, password, role){
  const response = await fetch("http://localhost:8080/users/register", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name, email, password, role })
  });

  if (!response.ok) {
    throw new Error("Erro ao registrar usuário!");
  }

  return response.json();
}