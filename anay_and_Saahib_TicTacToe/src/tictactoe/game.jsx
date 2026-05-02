import React from "react";
import { createRoot } from "react-dom/client";
import GameBoard from "./components/GameBoard";
import Layout from "./components/Layout";
import "./styles/base.css";
import "./styles/neon.css";
const root = createRoot(document.getElementById("root"));
root.render(
  <Layout>
    <GameBoard />
  </Layout>
);
