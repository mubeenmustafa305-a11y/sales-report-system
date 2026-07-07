<template>
  <div>
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-6">
      <div>
        <h2 class="text-2xl font-bold text-slate-900">Reports</h2>
        <p class="text-sm text-slate-500 mt-1">Generate and monitor asynchronous sales reports</p>
      </div>
      <button
        @click="handleGenerate"
        :disabled="store.generating"
        class="inline-flex items-center justify-center rounded-lg bg-brand-600 px-4 py-2.5 text-sm font-medium text-white shadow-sm hover:bg-brand-700 disabled:opacity-60 disabled:cursor-not-allowed transition-colors"
      >
        <svg v-if="store.generating" class="animate-spin -ml-1 mr-2 h-4 w-4" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
        </svg>
        {{ store.generating ? 'Generating...' : 'Generate Report' }}
      </button>
    </div>

    <div v-if="store.error" class="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
      {{ store.error }}
    </div>

    <div v-if="store.loading && !store.reports.length" class="flex justify-center py-16">
      <div class="h-8 w-8 animate-spin rounded-full border-4 border-brand-600 border-t-transparent" />
    </div>

    <div v-else-if="!store.reports.length" class="rounded-xl border border-dashed border-slate-300 bg-white p-12 text-center">
      <p class="text-slate-500">No reports yet. Click "Generate Report" to start.</p>
    </div>

    <div v-else class="overflow-hidden rounded-xl border border-slate-200 bg-white shadow-sm">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-slate-200">
          <thead class="bg-slate-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-slate-500">ID</th>
              <th class="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-slate-500">Status</th>
              <th class="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-slate-500">Created</th>
              <th class="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-slate-500">Completed</th>
              <th class="px-6 py-3 text-right text-xs font-medium uppercase tracking-wider text-slate-500">Actions</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-200">
            <tr v-for="report in sortedReports" :key="report.id" class="hover:bg-slate-50 transition-colors">
              <td class="whitespace-nowrap px-6 py-4 text-sm font-medium text-slate-900">#{{ report.id }}</td>
              <td class="whitespace-nowrap px-6 py-4 text-sm">
                <StatusBadge :status="report.status" />
              </td>
              <td class="whitespace-nowrap px-6 py-4 text-sm text-slate-500">{{ formatDate(report.createdAt) }}</td>
              <td class="whitespace-nowrap px-6 py-4 text-sm text-slate-500">
                {{ report.completedAt ? formatDate(report.completedAt) : '—' }}
              </td>
              <td class="whitespace-nowrap px-6 py-4 text-right text-sm">
                <router-link
                  :to="`/reports/${report.id}`"
                  class="font-medium text-brand-600 hover:text-brand-700"
                >View</router-link>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <p v-if="polling" class="mt-3 text-xs text-slate-400 flex items-center gap-1">
      <span class="h-1.5 w-1.5 rounded-full bg-emerald-500 animate-pulse" />
      Auto-refreshing every 3 seconds
    </p>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useReportsStore } from '../stores/reports'
import StatusBadge from '../components/StatusBadge.vue'

const store = useReportsStore()
const router = useRouter()
const polling = ref(false)
let intervalId = null

const sortedReports = computed(() =>
  [...store.reports].sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
)

function formatDate(iso) {
  if (!iso) return '—'
  return new Date(iso).toLocaleString()
}

async function handleGenerate() {
  try {
    const created = await store.generate()
    router.push(`/reports/${created.id}`)
  } catch {
    // error shown via store
  }
}

function startPolling() {
  if (intervalId) return
  polling.value = true
  intervalId = setInterval(async () => {
    await store.loadReports()
    if (!store.hasInProgressReports()) {
      stopPolling()
    }
  }, 3000)
}

function stopPolling() {
  polling.value = false
  if (intervalId) {
    clearInterval(intervalId)
    intervalId = null
  }
}

onMounted(async () => {
  await store.loadReports()
  if (store.hasInProgressReports()) {
    startPolling()
  }
})

onUnmounted(stopPolling)
</script>
