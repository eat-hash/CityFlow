<template>
  <div class="traffic">
    <el-card>
      <template #header>
        <span>路口流量趋势（模拟24小时）</span>
        <el-select v-model="selectedId" placeholder="选择路口" style="float: right; width: 200px">
          <el-option v-for="item in intersections" :key="item.intersectionId" :label="item.intersectionId" :value="item.intersectionId"></el-option>
        </el-select>
      </template>
      <div ref="lineChart" style="height: 500px"></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import * as echarts from 'echarts'
import { getTestData } from '@/api/traffic'

const intersections = ref([])
const selectedId = ref('')
const lineChart = ref(null)
let chart = null

// 生成模拟24小时流量数据（高峰早8点、晚6点）
const generateMockFlow = (baseFlow) => {
  const hours = Array.from({ length: 24 }, (_, i) => `${i}:00`)
  const flows = []
  for (let i = 0; i < 24; i++) {
    let factor = 0.5
    if (i >= 7 && i <= 9) factor = 1.8
    else if (i >= 17 && i <= 19) factor = 2.0
    else if (i >= 22 || i <= 5) factor = 0.3
    else factor = 0.8
    flows.push(Math.round(baseFlow * factor))
  }
  return { hours, flows }
}

const drawChart = (baseFlow) => {
  if (!chart && lineChart.value) chart = echarts.init(lineChart.value)
  const { hours, flows } = generateMockFlow(baseFlow || 500)
  chart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: hours },
    yAxis: { type: 'value', name: '车流量 (辆/小时)' },
    series: [{ type: 'line', data: flows, smooth: true, areaStyle: { opacity: 0.3 } }]
  })
}

onMounted(async () => {
  intersections.value = await getTestData()
  if (intersections.value.length) {
    selectedId.value = intersections.value[0].intersectionId
    // 计算该路口总流量作为基准
    const flow = intersections.value[0].trafficFlow
    const total = flow.EAST_WEST_STRAIGHT + flow.EAST_WEST_LEFT + flow.NORTH_SOUTH_STRAIGHT + flow.NORTH_SOUTH_LEFT
    drawChart(total)
  }
})

watch(selectedId, (newId) => {
  const intersection = intersections.value.find(i => i.intersectionId === newId)
  if (intersection) {
    const flow = intersection.trafficFlow
    const total = flow.EAST_WEST_STRAIGHT + flow.EAST_WEST_LEFT + flow.NORTH_SOUTH_STRAIGHT + flow.NORTH_SOUTH_LEFT
    drawChart(total)
  }
})
</script>