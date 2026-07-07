import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      '/reports': 'http://localhost:8080',
      '/callback': 'http://localhost:8080',
      '/mock': 'http://localhost:8080'
    }
  }
})
