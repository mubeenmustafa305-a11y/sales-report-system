import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '',
  timeout: 15000,
  headers: { 'Content-Type': 'application/json' }
})

export async function fetchReports() {
  const { data } = await api.get('/reports')
  return data
}

export async function fetchReport(id) {
  const { data } = await api.get(`/reports/${id}`)
  return data
}

export async function generateReport() {
  const { data } = await api.post('/reports')
  return data
}

export default api
