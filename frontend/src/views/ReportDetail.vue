<template>
  <div>
    <router-link to="/" class="inline-flex items-center text-sm text-brand-600 hover:text-brand-700 mb-4">
      &larr; Back to reports
    </router-link>

    <div v-if="store.loading && !store.currentReport" class="flex justify-center py-16">
      <div class="h-8 w-8 animate-spin rounded-full border-4 border-brand-600 border-t-transparent" />
    </div>

    <div v-else-if="store.error" class="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
      {{ store.error }}
    </div>

    <template v-else-if="report">
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-6">
        <div>
          <h2 class="text-2xl font-bold text-slate-900">Report #{{ report.id }}</h2>
          <div class="mt-2 flex items-center gap-3">
            <StatusBadge :status="report.status" />
            <span class="text-sm text-slate-500">Created {{ formatDate(report.createdAt) }}</span>
          </div>
        </div>
      </div>

      <div v-if="isInProgress" class="mb-6 rounded-lg border border-blue-200 bg-blue-50 px-4 py-3 text-sm text-blue-800 flex items-center gap-2">
        <div class="h-4 w-4 animate-spin rounded-full border-2 border-blue-600 border-t-transparent" />
        Report is being generated. This page refreshes every 3 seconds.
      </div>

      <div v-if="report.status === 'FAILED'" class="mb-6 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
        {{ report.errorMessage || 'Report generation failed.' }}
      </div>

      <template v-if="report.status === 'COMPLETED' && report.aggregatedData">
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 mb-8">
          <StatCard title="Total Revenue" :value="formatCurrency(summary.totalRevenue)" subtitle="Across all channels" />
          <StatCard title="Total Customers" :value="summary.totalCustomers?.toLocaleString()" subtitle="Active base" />
          <StatCard title="Total Orders" :value="summary.totalSales?.toLocaleString()" subtitle="Sales volume" />
          <StatCard title="Discounts Applied" :value="formatCurrency(summary.totalDiscountAmount)" subtitle="Total savings" />
          <StatCard title="Top Region" :value="summary.topRegion || '—'" subtitle="By sales amount" />
          <StatCard title="Primary Partner" :value="summary.primaryPartnerName || '—'" subtitle="Lead partner" />
        </div>

        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
          <div class="rounded-xl border border-slate-200 bg-white p-5 shadow-sm">
            <h3 class="text-sm font-semibold text-slate-900 mb-4">Monthly Revenue</h3>
            <RevenueBarChart :data="report.revenueData?.monthlyRevenue || []" />
          </div>
          <div class="rounded-xl border border-slate-200 bg-white p-5 shadow-sm">
            <h3 class="text-sm font-semibold text-slate-900 mb-4">Sales by Region</h3>
            <SalesRegionChart :data="report.salesData?.salesByRegion || []" />
          </div>
          <div class="rounded-xl border border-slate-200 bg-white p-5 shadow-sm lg:col-span-2">
            <h3 class="text-sm font-semibold text-slate-900 mb-4">Customer Distribution</h3>
            <CustomerPieChart :data="report.customerData?.segments || []" />
          </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <DataCard title="Partner" :data="report.partnerData" />
          <DataCard title="Sales" :data="report.salesData" />
          <DataCard title="Customers" :data="report.customerData" />
          <DataCard title="Revenue" :data="report.revenueData" />
          <DataCard title="Discounts" :data="report.discountData" />
        </div>
      </template>
    </template>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, defineComponent, h } from 'vue'
import { useReportsStore } from '../stores/reports'
import StatusBadge from '../components/StatusBadge.vue'
import StatCard from '../components/StatCard.vue'
import RevenueBarChart from '../components/charts/RevenueBarChart.vue'
import SalesRegionChart from '../components/charts/SalesRegionChart.vue'
import CustomerPieChart from '../components/charts/CustomerPieChart.vue'

const props = defineProps({ id: { type: [String, Number], required: true } })
const store = useReportsStore()
let intervalId = null

const report = computed(() => store.currentReport)
const summary = computed(() => report.value?.aggregatedData?.summary || {})
const isInProgress = computed(() => report.value && !['COMPLETED', 'FAILED'].includes(report.value.status))

const DataCard = defineComponent({
  props: { title: String, data: Object },
  setup(props) {
    return () => h('div', { class: 'rounded-xl border border-slate-200 bg-white p-5 shadow-sm' }, [
      h('h3', { class: 'text-sm font-semibold text-slate-900 mb-3' }, props.title),
      h('pre', { class: 'text-xs text-slate-600 overflow-auto max-h-48 bg-slate-50 rounded-lg p-3' },
        JSON.stringify(props.data, null, 2))
    ])
  }
})

function formatDate(iso) {
  return iso ? new Date(iso).toLocaleString() : '—'
}

function formatCurrency(val) {
  if (val == null) return '—'
  return `$${Number(val).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`
}

async function refresh() {
  await store.loadReport(props.id)
  if (store.currentReport && ['COMPLETED', 'FAILED'].includes(store.currentReport.status)) {
    stopPolling()
  }
}

function startPolling() {
  intervalId = setInterval(refresh, 3000)
}

function stopPolling() {
  if (intervalId) {
    clearInterval(intervalId)
    intervalId = null
  }
}

onMounted(async () => {
  await store.loadReport(props.id)
  if (isInProgress.value) startPolling()
})

onUnmounted(stopPolling)
</script>
