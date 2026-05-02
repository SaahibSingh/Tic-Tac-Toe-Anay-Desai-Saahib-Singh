import React, { useEffect, useState } from "react";
import { apiGetLeaderboard } from "../api";
export default function Leaderboard() {
  const [rows, setRows] = useState([]);

  useEffect(() => {
    apiGetLeaderboard().then(setRows);
  }, []);

  return (
    <div className="center-box neon-border">
      <h2 className="neon-text">Leaderboard</h2>

      {rows.length === 0 ? (
        <p>No games recorded yet.</p>
      ) : (
        <ul className="leader-list">
          {rows.map((r, i) => (
            <li key={i}>{r}</li>
          ))}
        </ul>
      )}
    </div>
  );
}
