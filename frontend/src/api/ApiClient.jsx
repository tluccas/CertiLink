export function ApiClient(token) {
  return (url, options = {}) =>
    fetch("http://localhost:8080" + url, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
        ...options.headers,
      },
    });
}
