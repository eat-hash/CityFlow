<template>
  <div class="timing">
    <el-card>
      <template #header>历史配时方案</template>
      <el-table :data="plans" border stripe>
        <el-table-column prop="id" label="方案ID"></el-table-column>
        <el-table-column prop="intersectionId" label="路口ID"></el-table-column>
        <el-table-column prop="cycleLength" label="周期(秒)"></el-table-column>
        <el-table-column prop="averageDelay" label="平均延误(秒)"></el-table-column>
        <el-table-column prop="createdAt" label="创建时间"></el-table-column>
        <el-table-column label="操作">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="applyPlan(row)">应用</el-button>
            <el-button type="danger" size="small" @click="deletePlan(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const plans = ref([])

const loadPlans = () => {
  const stored = localStorage.getItem('timingPlans')
  plans.value = stored ? JSON.parse(stored) : []
}

const applyPlan = (plan) => {
  ElMessage.success(`已应用路口${plan.intersectionId}的方案（模拟下发）`)
}

const deletePlan = (id) => {
  let arr = plans.value.filter(p => p.id !== id)
  localStorage.setItem('timingPlans', JSON.stringify(arr))
  loadPlans()
  ElMessage.success('删除成功')
}

onMounted(loadPlans)
</script>