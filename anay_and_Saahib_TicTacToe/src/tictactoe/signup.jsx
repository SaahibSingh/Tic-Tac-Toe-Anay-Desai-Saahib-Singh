import React from "react";
import { createRoot } from "react-dom/client";
import Signup from "./components/Signup";
import "./styles/base.css";
import "./styles/neon.css";
const root = createRoot(document.getElementById("root"));
root.render(<Signup />);
