import React, { useEffect, useState } from "react";
import { apiGetGame, apiPlayMove, apiReset, apiLogout } from "../api";
import NeonButton from "./NeonButton";

export default function GameBoard() {
  const [grid, setGrid] = useState([["E","E","E"],["E","E","E"],["E","E","E"]]);
  const [message, setMessage] = useState("");
  const [gameOver, setGameOver] = useState(false);

  async function loadGame() {
    const data = await apiGetGame();
    setGrid(data.grid);
    setMessage(data.message || "");
    setGameOver(data.gameOver || false);
  }

  useEffect(() => {
    loadGame();
  }, []);

  async function handleCellClick(r, c) {
    if (gameOver || grid[r][c] !== "E") return;
    const data = await apiPlayMove(r, c);
    setGrid(data.grid);
    setMessage(data.message || "");
    setGameOver(data.gameOver || false);
  }

  async function handleReset() {
    await apiReset();
    await loadGame();
  }

  async function handleLogout() {
    await apiLogout();
    window.location.href = "/login";
  }

  return (
    <div className="game-container neon-border">
      <h2 className="neon-text">Tic‑Tac‑Toe Arena</h2>
      <div className="message">{message}</div>

      <table className="board">
        <tbody>
          {grid.map((row, r) => (
            <tr key={r}>
              {row.map((cell, c) => (
                <td key={c} onClick={() => handleCellClick(r, c)}>
                  {cell === "E" ? "" : (
                    <span className={cell === "X" ? "x-cell" : "o-cell"}>
                      {cell}
                    </span>
                  )}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>

      <div className="controls">
        <NeonButton onClick={handleReset}>New Game</NeonButton>
        <NeonButton onClick={handleLogout}>Logout</NeonButton>
      </div>
    </div>
  );
}
