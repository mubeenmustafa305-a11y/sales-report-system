<template>
  <v-chart class="h-72 w-full" :option="option" autoresize />
</template>

<script setup>
import { computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import VChart from 'vue-echarts'

use([CanvasRenderer, BarChart, GridComponent, TooltipComponent, LegendComponent])

const props = defineProps({
  data: { type: Array, default: () => [] }
})

const option = computed(() => ({
  tooltip: { trigger: 'axis' },
  grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
  xAxis: {
    type: 'category',
    data: props.data.map(d => d.month),
    axisLabel: { color: '#64748b' }
  },
  yAxis: {
    type: 'value',
    axisLabel: {
      color: '#64748b',
      formatter: val => `$${(val / 1000).toFixed(0)}k`
    }
  },
  series: [{
    name: 'Revenue',
    type: 'bar',
    data: props.data.map(d => d.revenue),
    itemStyle: { color: '#2563eb', borderRadius: [4, 4, 0, 0] }
  }]
}))
</script>
