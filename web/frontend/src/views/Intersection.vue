<template>
  <div class="intersection-container">
    <el-card class="box-card">
      <template #header>
        <span>路口列表</span>
        <el-button type="primary" size="small" style="float: right" @click="batchOptimize">批量优化</el-button>
      </template>
      <el-table :data="intersectionList" border stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="intersectionId" label="路口ID" width="120"></el-table-column>
        <el-table-column label="东-西直行" prop="trafficFlow.EAST_WEST_STRAIGHT"></el-table-column>
        <el-table-column label="东-西左转" prop="trafficFlow.EAST_WEST_LEFT"></el-table-column>
        <el-table-column label="南-北直行" prop="trafficFlow.NORTH_SOUTH_STRAIGHT"></el-table-column>
        <el-table-column label="南-北左转" prop="trafficFlow.NORTH_SOUTH_LEFT"></el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleOptimizeSingle(row)">优化</el-button>
            <el-button type="info" size="small" @click="viewDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 优化结果对话框 -->
    <el-dialog v-model="dialogVisible" title="配时优化结果" width="800px">
      <div v-if="optimizeResult">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="路口ID">{{ optimizeResult.intersectionId }}</el-descriptions-item>
          <el-descriptions-item label="周期时长">{{ optimizeResult.cycleLength }}秒</el-descriptions-item>
          <el-descriptions-item label="平均延误">{{ optimizeResult.averageDelay }}秒</el-descriptions-item>
          <el-descriptions-item label="通行能力">{{ optimizeResult.capacity }}%</el-descriptions-item>
        </el-descriptions>
        <div ref="chartRef" style="height: 400px; margin-top: 20px"></div>
      </div>
      <template #footer>
        <el-button @click="dialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="savePlan">保存方案</el-button>
      </template>
    </el-dialog>

    <!-- 批量优化弹窗（实际批量优化已整合到主表格选择，可移除或保留演示） -->
    <el-dialog v-model="batchDialogVisible" title="批量优化结果" width="600px">
      <el-table :data="batchResults" border>
        <el-table-column prop="intersectionId" label="路口ID"></el-table-column>
        <el-table-column prop="cycleLength" label="周期(秒)"></el-table-column>
        <el-table-column prop="averageDelay" label="延误(秒)"></el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="batchDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import { getTestData, optimizeSingle as optimizeSingleApi, optimizeBatch as optimizeBatchApi } from '@/api/traffic'

const intersectionList = ref([])
const dialogVisible = ref(false)
const batchDialogVisible = ref(false)
const optimizeResult = ref(null)
const selectedRows = ref([])      // 批量选中的路口
const batchResults = ref([])       // 批量优化结果存储
const chartRef = ref(null)
let chart = null

onMounted(async () => {
  const data = await getTestData()
  intersectionList.value = data
})

// 表格多选变化
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

// 单个路口优化（不与导入函数重名）
const handleOptimizeSingle = async (row) => {
  const params = {
    intersectionId: row.intersectionId,
    trafficFlow: row.trafficFlow,
    minGreen: 15,
    maxCycle: 120
  }
  try {
    const res = await optimizeSingleApi(params)
    optimizeResult.value = res
    dialogVisible.value = true
    await nextTick()
    drawChart(res.timingPlan)
  } catch (error) {
    ElMessage.error('优化失败，请检查后端服务')
  }
}

// 绘制柱状图
const drawChart = (timingPlan) => {
  if (chart) chart.dispose()
  if (!chartRef.value) return
  chart = echarts.init(chartRef.value)
  const phases = ['东西直行', '东西左转', '南北直行', '南北左转']
  const optimizedData = [
    timingPlan.EAST_WEST_STRAIGHT,
    timingPlan.EAST_WEST_LEFT,
    timingPlan.NORTH_SOUTH_STRAIGHT,
    timingPlan.NORTH_SOUTH_LEFT
  ]
  const originalData = [35, 15, 40, 10]
  chart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: phases },
    yAxis: { type: 'value', name: '绿灯时长 (秒)' },
    series: [
      { name: '优化前', type: 'bar', data: originalData, itemStyle: { color: '#aaa' } },
      { name: '优化后', type: 'bar', data: optimizedData, itemStyle: { color: '#67C23A' } }
    ]
  })
}

// 保存方案到 localStorage
const savePlan = () => {
  const plans = JSON.parse(localStorage.getItem('timingPlans') || '[]')
  plans.push({
    id: Date.now(),
    intersectionId: optimizeResult.value.intersectionId,
    timingPlan: optimizeResult.value.timingPlan,
    cycleLength: optimizeResult.value.cycleLength,
    averageDelay: optimizeResult.value.averageDelay,
    createdAt: new Date().toLocaleString()
  })
  localStorage.setItem('timingPlans', JSON.stringify(plans))
  ElMessage.success('方案已保存')
}

// 批量优化（使用表格选中的路口）
const batchOptimize = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请至少选择一个路口')
    return
  }
  const list = selectedRows.value.map(item => ({
    intersectionId: item.intersectionId,
    trafficFlow: item.trafficFlow
  }))
  try {
    const results = await optimizeBatchApi(list)
    batchResults.value = results
    batchDialogVisible.value = true
    let msg = '批量优化完成：\n'
    results.forEach(r => {
      msg += `${r.intersectionId} 周期=${r.cycleLength}s 延误=${r.averageDelay}s\n`
    })
    ElMessage.success(msg)
  } catch (error) {
    ElMessage.error('批量优化失败')
  }
}

// 查看详情（简单提示）
const viewDetail = (row) => {
  ElMessage.info(`路口 ${row.intersectionId} 详情功能开发中`)
}
</script>

<style scoped>
.intersection-container { padding: 20px; }
</style>