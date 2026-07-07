<template>
  <span :class="badgeClass">{{ status }}</span>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  status: { type: String, required: true }
})

const badgeClass = computed(() => {
  const base = 'inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium'
  const map = {
    PENDING: 'bg-slate-100 text-slate-700',
    PROCESSING: 'bg-blue-100 text-blue-800',
    WAITING_CALLBACK: 'bg-amber-100 text-amber-800',
    COLLECTING_DATA: 'bg-indigo-100 text-indigo-800',
    COMPLETED: 'bg-emerald-100 text-emerald-800',
    FAILED: 'bg-red-100 text-red-800'
  }
  return `${base} ${map[props.status] || 'bg-slate-100 text-slate-700'}`
})
</script>
