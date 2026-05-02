import React from "react";
export default function NeonButton({ children, ...props }) {
  return (
    <button className="btn neon-btn" {...props}>
      {children}
    </button>
  );
}
