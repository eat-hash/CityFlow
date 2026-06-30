<template>
  <div class="visualization-container">
    <el-row :gutter="20" class="toolbar">
      <el-col :span="6">
        <el-select v-model="selectedIntersectionId" placeholder="选择路口" clearable @change="onIntersectionChange">
          <el-option
            v-for="item in intersectionList"
            :key="item.intersectionId"
            :label="`路口 ${item.intersectionId}`"
            :value="item.intersectionId"
          />
        </el-select>
      </el-col>
      <el-col :span="6">
        <el-button type="primary" @click="refreshAllCharts" :loading="loading">刷新所有图表</el-button>
      </el-col>
      <el-col :span="12" class="tip-text">
        <span>📊 配时对比图会调用后端优化接口，实时计算最优配时方案</span>
      </el-col>
    </el-row>

    <!-- 第一行：散点图 + 仪表盘 -->
    <el-row :gutter="20">
      <el-col :span="14">
        <el-card class="chart-card">
          <template #header>
            <span>🚦 路口拥堵散点图</span>
            <span style="font-size: 12px; color: #888; margin-left: 10px;">点大小表示车流量</span>
          </template>
          <div ref="scatterChartRef" style="height: 400px; width: 100%;"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card class="chart-card">
          <template #header>
            <span>⚙️ 通行能力仪表盘</span>
            <span style="font-size: 12px; color: #888;">{{ selectedIntersectionId ? `路口 ${selectedIntersectionId}` : '未选择路口' }}</span>
          </template>
          <div ref="gaugeChartRef" style="height: 350px; width: 100%;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行：配时对比柱状图 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card class="chart-card">
          <template #header>
            <span>⏱️ 配时优化对比（单位：秒）</span>
            <span v-if="optimizedTimingPlan" style="margin-left: 20px; font-size: 12px; color: #67C23A;">
              周期时长：{{ currentCycleLength }}秒 | 平均延误：{{ currentAverageDelay }}秒
            </span>
          </template>
          <div v-if="timingChartLoading" class="chart-loading">正在调用优化算法...</div>
          <div ref="timingChartRef" style="height: 400px; width: 100%;" v-show="!timingChartLoading"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第三行：流量趋势折线图 -->
    <el-row :gutter="20" style="margin-top: 20px; margin-bottom: 20px;">
      <el-col :span="24">
        <el-card class="chart-card">
          <template #header>
            <span>📈 路口流量趋势（模拟24小时）</span>
            <span style="font-size: 12px; color: #888;">基于真实流量数据生成</span>
          </template>
          <div ref="trendChartRef" style="height: 400px; width: 100%;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, watch } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import { getTestData, optimizeSingle } from '@/api/traffic'

// 数据状态
const intersectionList = ref([])          // 所有路口数据
const selectedIntersectionId = ref('')     // 当前选中的路口ID
const loading = ref(false)                 // 全局刷新loading
const timingChartLoading = ref(false)      // 配时图专项loading

// 当前选中路口的优化结果
const optimizedTimingPlan = ref(null)
const currentCycleLength = ref(0)
const currentAverageDelay = ref(0)
const currentTotalFlow = ref(0)

// 图表实例
let scatterChart = null
let gaugeChart = null
let timingChart = null
let trendChart = null

// DOM 引用
const scatterChartRef = ref(null)
const gaugeChartRef = ref(null)
const timingChartRef = ref(null)
const trendChartRef = ref(null)

// 工具函数：获取路口的总流量
const getTotalFlow = (trafficFlow) => {
  if (!trafficFlow) return 0
  return (trafficFlow.EAST_WEST_STRAIGHT || 0) +
         (trafficFlow.EAST_WEST_LEFT || 0) +
         (trafficFlow.NORTH_SOUTH_STRAIGHT || 0) +
         (trafficFlow.NORTH_SOUTH_LEFT || 0)
}

// 生成模拟24小时流量曲线（基于实际总流量*动态系数）
const generateMockTrendData = (totalFlow) => {
  const hours = Array.from({ length: 24 }, (_, i) => `${i}:00`)
  const flows = []
  for (let i = 0; i < 24; i++) {
    let factor = 0.5
    if (i >= 7 && i <= 9) factor = 1.8      // 早高峰
    else if (i >= 17 && i <= 19) factor = 2.0 // 晚高峰
    else if (i >= 22 || i <= 5) factor = 0.3   // 夜间
    else factor = 0.8                          // 平峰
    flows.push(Math.round(totalFlow * factor))
  }
  return { hours, flows }
}

// ==================== 1. 散点图 ====================
const renderScatterChart = () => {
  if (!scatterChartRef.value) return
  if (scatterChart) scatterChart.dispose()
  scatterChart = echarts.init(scatterChartRef.value)
  
  const data = intersectionList.value.map((item, idx) => {
    const total = getTotalFlow(item.trafficFlow)
    return {
      name: item.intersectionId,
      value: [idx + 1, total, total]   // [x轴序号, y轴流量, 额外数值]
    }
  })
  
  const option = {
    tooltip: {
      formatter: (params) => {
        return `路口 ${params.data.name}<br/>总流量: ${params.data.value[1]} 辆/小时`
      }
    },
    xAxis: { name: '路口编号', type: 'value' },
    yAxis: { name: '总流量 (辆/小时)' },
    series: [{
      type: 'scatter',
      data: data,
      symbolSize: (val) => Math.max(15, val[2] / 20), // 点大小与流量正相关
      label: {
        show: true,
        formatter: (params) => params.data.name,
        position: 'top',
        offset: [0, 10]
      },
      itemStyle: {
        color: (params) => {
          const flow = params.data.value[1]
          if (flow > 1200) return '#F56C6C'   // 严重拥堵红
          if (flow > 800) return '#E6A23C'    // 预警黄
          return '#67C23A'                    // 畅通绿
        }
      }
    }]
  }
  scatterChart.setOption(option)
}

// ==================== 2. 仪表盘 ====================
const renderGaugeChart = () => {
  if (!gaugeChartRef.value) return
  if (gaugeChart) gaugeChart.dispose()
  gaugeChart = echarts.init(gaugeChartRef.value)
  
  // 通行能力基于流量反向计算（假设最大容量为1500辆/小时）
  let capacityPercent = 0
  if (selectedIntersectionId.value && currentTotalFlow.value > 0) {
    capacityPercent = Math.min(100, (currentTotalFlow.value / 1500) * 100)
  } else {
    capacityPercent = 0
  }
  
  const option = {
    series: [{
      type: 'gauge',
      center: ['50%', '55%'],
      radius: '80%',
      min: 0,
      max: 100,
      splitNumber: 5,
      progress: { show: true, width: 18, itemStyle: { color: '#409EFF' } },
      axisLine: { lineStyle: { width: 18, color: [[0.6, '#67C23A'], [0.8, '#E6A23C'], [1, '#F56C6C']] } },
      axisTick: { show: false },
      splitLine: { show: false },
      axisLabel: { show: false },
      pointer: { show: false },
      detail: { offsetCenter: [0, 20], valueAnimation: true, fontSize: 20, formatter: '{value}%' },
      title: { show: true, offsetCenter: [0, -20], fontSize: 14 },
      data: [{ value: Math.round(capacityPercent), name: '通行能力' }]
    }]
  }
  gaugeChart.setOption(option)
}

// ==================== 3. 配时对比柱状图 ====================
const renderTimingChart = async () => {
  if (!selectedIntersectionId.value) {
    // 如果没有选中路口，显示提示信息
    if (timingChart) timingChart.clear()
    return
  }
  
  timingChartLoading.value = true
  try {
    // 找到选中的路口数据
    const intersection = intersectionList.value.find(i => i.intersectionId === selectedIntersectionId.value)
    if (!intersection) throw new Error('路口数据不存在')
    
    // 调用后端优化接口
    const params = {
      intersectionId: intersection.intersectionId,
      trafficFlow: intersection.trafficFlow,
      minGreen: 15,
      maxCycle: 120
    }
    const result = await optimizeSingle(params)
    optimizedTimingPlan.value = result.timingPlan
    currentCycleLength.value = result.cycleLength
    currentAverageDelay.value = result.averageDelay
    currentTotalFlow.value = getTotalFlow(intersection.trafficFlow)
    
    // 构造优化后数据
    const optimizedData = [
      optimizedTimingPlan.value.EAST_WEST_STRAIGHT,
      optimizedTimingPlan.value.EAST_WEST_LEFT,
      optimizedTimingPlan.value.NORTH_SOUTH_STRAIGHT,
      optimizedTimingPlan.value.NORTH_SOUTH_LEFT
    ]
    // 模拟优化前的固定配时（实际可由后端提供，此处模拟典型值）
    const originalData = [35, 15, 40, 10]
    
    if (!timingChartRef.value) return
    if (timingChart) timingChart.dispose()
    timingChart = echarts.init(timingChartRef.value)
    
    const option = {
      tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
      legend: { data: ['优化前', '优化后'] },
      xAxis: { type: 'category', data: ['东西直行', '东西左转', '南北直行', '南北左转'] },
      yAxis: { type: 'value', name: '绿灯时长 (秒)' },
      series: [
        { name: '优化前', type: 'bar', data: originalData, itemStyle: { color: '#aaa' } },
        { name: '优化后', type: 'bar', data: optimizedData, itemStyle: { color: '#67C23A' } }
      ]
    }
    timingChart.setOption(option)
    
    // 仪表盘也需要更新
    renderGaugeChart()
    
  } catch (error) {
    console.error('配时优化失败', error)
    ElMessage.error('配时优化计算失败，请检查后端服务')
  } finally {
    timingChartLoading.value = false
  }
}

// ==================== 4. 流量趋势折线图 ====================
const renderTrendChart = () => {
  if (!trendChartRef.value) return
  if (!selectedIntersectionId.value) return
  
  const intersection = intersectionList.value.find(i => i.intersectionId === selectedIntersectionId.value)
  if (!intersection) return
  
  const totalFlow = getTotalFlow(intersection.trafficFlow)
  const { hours, flows } = generateMockTrendData(totalFlow)
  
  if (trendChart) trendChart.dispose()
  trendChart = echarts.init(trendChartRef.value)
  
  const option = {
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: hours, boundaryGap: false, axisLabel: { rotate: 45, interval: 3 } },
    yAxis: { type: 'value', name: '车流量 (辆/小时)' },
    series: [{
      type: 'line',
      data: flows,
      smooth: true,
      areaStyle: { opacity: 0.3, color: '#409EFF' },
      lineStyle: { color: '#409EFF', width: 2 },
      symbol: 'circle',
      symbolSize: 4
    }],
    grid: { containLabel: true, left: 50, right: 30, bottom: 30 }
  }
  trendChart.setOption(option)
}

// 当选中路口变化时，刷新所有依赖该路口的图表
const onIntersectionChange = () => {
  if (!selectedIntersectionId.value) return
  renderTimingChart()    // 重新调用优化接口并绘制配时图
  renderTrendChart()     // 刷新趋势图
}

// 刷新所有图表（包括散点图和仪表盘）
const refreshAllCharts = async () => {
  loading.value = true
  try {
    // 重新获取路口列表（如果后端数据可能更新）
    const data = await getTestData()
    intersectionList.value = data
    if (!selectedIntersectionId.value && data.length > 0) {
      selectedIntersectionId.value = data[0].intersectionId
    }
    renderScatterChart()
    if (selectedIntersectionId.value) {
      await renderTimingChart()  // 等待优化结果返回
      renderTrendChart()
    } else {
      if (timingChart) timingChart.clear()
      if (trendChart) trendChart.clear()
      renderGaugeChart()
    }
    ElMessage.success('图表已刷新')
  } catch (error) {
    ElMessage.error('获取数据失败，请检查后端接口')
  } finally {
    loading.value = false
  }
}

// 页面自适应
const handleResize = () => {
  [scatterChart, gaugeChart, timingChart, trendChart].forEach(chart => {
    if (chart) chart.resize()
  })
}

// 初始化数据
onMounted(async () => {
  try {
    const data = await getTestData()
    intersectionList.value = data
    if (data.length > 0) {
      selectedIntersectionId.value = data[0].intersectionId
    }
    await nextTick()
    renderScatterChart()
    renderGaugeChart()
    if (selectedIntersectionId.value) {
      await renderTimingChart()
      renderTrendChart()
    }
    window.addEventListener('resize', handleResize)
  } catch (error) {
    ElMessage.error('初始化数据失败，请确保后端服务运行在8081端口')
  }
})
</script>

<style scoped>
.visualization-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}
.toolbar {
  margin-bottom: 20px;
}
.tip-text {
  line-height: 32px;
  text-align: right;
  color: #606266;
  font-size: 13px;
}
.chart-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}
.chart-loading {
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: 14px;
}
</style>