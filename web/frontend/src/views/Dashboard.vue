<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6" v-for="stat in stats" :key="stat.title">
        <el-card shadow="hover">
          <div class="stat-title">{{ stat.title }}</div>
          <div class="stat-value">{{ stat.value }}</div>
        </el-card>
      </el-col>
    </el-row>
    <el-card style="margin-top: 20px">
      <template #header>路口拥堵散点图</template>
      <div ref="scatterChart" style="height: 500px"></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { getTestData } from '@/api/traffic'

const stats = ref([])
const scatterChart = ref(null)

onMounted(async () => {
  const data = await getTestData()
  const totalIntersections = data.length
  let totalFlowSum = 0
  const points = []
  data.forEach((item, idx) => {
    const flow = item.trafficFlow
    const sum = flow.EAST_WEST_STRAIGHT + flow.EAST_WEST_LEFT + flow.NORTH_SOUTH_STRAIGHT + flow.NORTH_SOUTH_LEFT
    totalFlowSum += sum
    points.push([idx + 1, sum, sum]) // [x, y, 流量]
  })
  const avgFlow = (totalFlowSum / totalIntersections).toFixed(0)
  stats.value = [
    { title: '总路口数', value: totalIntersections },
    { title: '平均流量 (辆/小时)', value: avgFlow },
    { title: '平均拥堵指数', value: (avgFlow / 500).toFixed(2) },
    { title: '绿波带覆盖率', value: '65%' }
  ]
  const chart = echarts.init(scatterChart.value)
  chart.setOption({
    tooltip: { formatter: '{b}<br/>流量: {c} 辆/小时' },
    xAxis: { name: '路口编号' },
    yAxis: { name: '总流量 (辆/小时)' },
    series: [{
      type: 'scatter',
      data: points,
      symbolSize: val => val[2] / 20,
      label: { show: true, formatter: '{b}', position: 'top' }
    }]
  })
})
</script>

<style scoped>
.dashboard { padding: 20px; }
.stat-title { font-size: 14px; color: #666; }
.stat-value { font-size: 28px; font-weight: bold; margin-top: 10px; }
</style>