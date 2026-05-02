import React from "react";
import { createRoot } from "react-dom/client";
import Matchmaking from "./components/Matchmaking";
import Layout from "./components/Layout";
import "./styles/base.css";
import "./styles/neon.css";
const root = createRoot(document.getElementById("root"));
root.render(
  <Layout>
    <Matchmaking />
  </Layout>
);
