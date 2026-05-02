import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import { resolve } from "path";

export default defineConfig({
  plugins: [react()],
  root: "./",
  build: {
    outDir: "../web/react-dist",
    emptyOutDir: true,
    rollupOptions: {
      input: {
        login: resolve(__dirname, "src/login.jsx"),
        signup: resolve(__dirname, "src/signup.jsx"),
        game: resolve(__dirname, "src/game.jsx"),
        leaderboard: resolve(__dirname, "src/leaderboard.jsx"),
        chat: resolve(__dirname, "src/chat.jsx"),
        matchmaking: resolve(__dirname, "src/matchmaking.jsx")
      },
      output: {
        entryFileNames: "[name].js",
        chunkFileNames: "chunks/[name]-[hash].js",
        assetFileNames: "assets/[name]-[hash][extname]"
      }
    }
  }
});
