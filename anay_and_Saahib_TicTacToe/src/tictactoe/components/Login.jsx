import React, { useState } from "react";
import { apiLogin } from "../api";
import NeonButton from "./NeonButton";
export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  async function handleSubmit(e) {
    e.preventDefault();
    const res = await apiLogin(username, password);
    if (res.ok) {
      window.location.href = "/game";
    } else {
      setError("Invalid username or password.");
    }
  }

  return (
    <div className="auth-card neon-border">
      <h2 className="neon-text">Sign In</h2>
      {error && <p className="error-text">{error}</p>}

      <form onSubmit={handleSubmit}>
        <input
          className="input"
          placeholder="Username"
          value={username}
          onChange={e => setUsername(e.target.value)}
          required
        />

        <input
          className="input"
          type="password"
          placeholder="Password"
          value={password}
          onChange={e => setPassword(e.target.value)}
          required
        />

        <NeonButton type="submit">Login</NeonButton>
      </form>

      <p className="small-text">
        New here? <a href="/signup">Create an account</a>
      </p>
    </div>
  );
}
