import React, { useState } from "react";
import { apiJoinMatchmaking } from "../api";
import NeonButton from "./NeonButton";

export default function Matchmaking() {
  const [status, setStatus] = useState("Click to find a match.");
  const [opponent, setOpponent] = useState(null);

  async function handleJoin() {
    setStatus("Searching for opponent...");
    const res = await apiJoinMatchmaking();

    if (res.opponent) {
      setOpponent(res.opponent);
      setStatus("Match found!");
    } else {
      setStatus("Waiting in queue...");
    }
  }

  return (
    <div className="center-box neon-border">
      <h2 className="neon-text">Matchmaking</h2>
      <p>{status}</p>
      {opponent && <p>Opponent: {opponent}</p>}
      <NeonButton onClick={handleJoin}>Find Match</NeonButton>
    </div>
  );
}
