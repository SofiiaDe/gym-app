import { defineConfig } from "vitest/config"
import react from "@vitejs/plugin-react"
import dns from "dns"

dns.setDefaultResultOrder("verbatim")

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: "localhost",
    open: true,
  },
  build: {
    outDir: "build",
    sourcemap: true,
  },
  test: {
    globals: true,
    environment: "jsdom",
    setupFiles: "src/setupTests",
    mockReset: true,
  },
})
