import { defineStore } from 'pinia'
import { ref } from 'vue'
import * as reportsApi from '../api/reports'

export const useReportsStore = defineStore('reports', () => {
  const reports = ref([])
  const currentReport = ref(null)
  const loading = ref(false)
  const generating = ref(false)
  const error = ref(null)

  async function loadReports() {
    loading.value = true
    error.value = null
    try {
      reports.value = await reportsApi.fetchReports()
    } catch (e) {
      error.value = e.response?.data?.message || e.message || 'Failed to load reports'
    } finally {
      loading.value = false
    }
  }

  async function loadReport(id) {
    loading.value = true
    error.value = null
    try {
      currentReport.value = await reportsApi.fetchReport(id)
    } catch (e) {
      error.value = e.response?.data?.message || e.message || 'Failed to load report'
      currentReport.value = null
    } finally {
      loading.value = false
    }
  }

  async function generate() {
    generating.value = true
    error.value = null
    try {
      const created = await reportsApi.generateReport()
      await loadReports()
      return created
    } catch (e) {
      error.value = e.response?.data?.message || e.message || 'Failed to generate report'
      throw e
    } finally {
      generating.value = false
    }
  }

  function hasInProgressReports() {
    return reports.value.some(r => !['COMPLETED', 'FAILED'].includes(r.status))
  }

  return {
    reports,
    currentReport,
    loading,
    generating,
    error,
    loadReports,
    loadReport,
    generate,
    hasInProgressReports
  }
})
