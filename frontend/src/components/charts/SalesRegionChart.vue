<template>
  <v-chart class="h-72 w-full" :option="option" autoresize />
</template>

<script setup>
import { computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent } from 'echarts/components'
import VChart from 'vue-echarts'

use([CanvasRenderer, BarChart, GridComponent, TooltipComponent])

const props = defineProps({
  data: { type: Array, default: () => [] }
})

const option = computed(() => ({
  tooltip: {
    trigger: 'axis',
    formatter: params => {
      const p = params[0]
      return `${p.name}<br/>Orders: ${props.data[p.dataIndex]?.orders}<br/>Amount: $${Number(p.value).toLocaleString()}`
    }
  },
  grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
  xAxis: {
    type: 'category',
    data: props.data.map(d => d.region),
    axisLabel: { color: '#64748b', rotate: 20 }
  },
  yAxis: {
    type: 'value',
    axisLabel: {
      color: '#64748b',
      formatter: val => `$${(val / 1000).toFixed(0)}k`
    }
  },
  series: [{
    name: 'Sales',
    type: 'bar',
    data: props.data.map(d => d.amount),
    itemStyle: { color: '#7c3aed', borderRadius: [4, 4, 0, 0] }
  }]
}))
</script>
