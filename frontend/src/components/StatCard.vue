<template>
  <div class="rounded-xl border border-slate-200 bg-white p-5 shadow-sm">
    <p class="text-sm font-medium text-slate-500">{{ title }}</p>
    <p class="mt-2 text-2xl font-semibold text-slate-900">{{ value }}</p>
    <p v-if="subtitle" class="mt-1 text-xs text-slate-400">{{ subtitle }}</p>
  </div>
</template>

<script setup>
defineProps({
  title: String,
  value: [String, Number],
  subtitle: String
})
</script>
