import React, { useState } from "react";
import { apiSignup } from "../api";
import NeonButton from "./NeonButton";
export default function Signup() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  async function handleSubmit(e) {
    e.preventDefault();
    const res = await apiSignup(username, password);
    if (res.ok) {
      window.location.href = "/login";
    } else {
      setError("Username already taken.");
    }
  }

  return (
    <div className="auth-card neon-border">
      <h2 className="neon-text">Create Account</h2>
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

        <NeonButton type="submit">Sign Up</NeonButton>
      </form>

      <p className="small-text">
        Already have an account? <a href="/login">Sign in</a>
      </p>
    </div>
  );
}
