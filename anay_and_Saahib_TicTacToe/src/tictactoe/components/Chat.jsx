import React, { useEffect, useState } from "react";
import { apiGetChat, apiPostChat } from "../api";
import NeonButton from "./NeonButton";

export default function Chat() {
  const [messages, setMessages] = useState([]);
  const [draft, setDraft] = useState("");

  async function loadChat() {
    const data = await apiGetChat();
    setMessages(data.messages || []);
  }

  useEffect(() => {
    loadChat();
    const id = setInterval(loadChat, 2000);
    return () => clearInterval(id);
  }, []);

  async function handleSend(e) {
    e.preventDefault();
    if (!draft.trim()) return;
    await apiPostChat(draft.trim());
    setDraft("");
    await loadChat();
  }

  return (
    <div className="center-box neon-border">
      <h2 className="neon-text">Global Chat</h2>

      <div className="chat-box">
        {messages.map((m, i) => (
          <div key={i} className="chat-line">{m}</div>
        ))}
      </div>

      <form onSubmit={handleSend}>
        <input
          className="input"
          placeholder="Type a message..."
          value={draft}
          onChange={e => setDraft(e.target.value)}
        />
        <NeonButton type="submit">Send</NeonButton>
      </form>
    </div>
  );
}
