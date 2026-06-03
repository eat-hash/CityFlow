<template>
  <el-container class="app-container">
    <!-- 左侧菜单（可折叠） -->
    <el-aside :width="isCollapse ? '64px' : '220px'" class="app-aside">
      <div class="logo-box">
        <span v-if="!isCollapse" class="logo-text">🚦 信号优化平台</span>
        <span v-else class="logo-icon">🚦</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="el-menu-vertical"
        :collapse="isCollapse"
        background-color="transparent"
        text-color="#bfcbd9"
        active-text-color="#00d4ff"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <template #title>首页概览</template>
        </el-menu-item>
        <el-menu-item index="/intersection">
          <el-icon><Location /></el-icon>
          <template #title>路口管理</template>
        </el-menu-item>
        <el-menu-item index="/traffic">
          <el-icon><DataLine /></el-icon>
          <template #title>流量监测</template>
        </el-menu-item>
        <el-menu-item index="/timing">
          <el-icon><Timer /></el-icon>
          <template #title>配时方案</template>
        </el-menu-item>
        <el-menu-item index="/visualization">
          <el-icon><PieChart /></el-icon>
          <template #title>数据可视化</template>
        </el-menu-item>
      </el-menu>
      <div class="collapse-btn" @click="toggleCollapse">
        <el-icon><Fold /></el-icon>
      </div>
    </el-aside>

    <!-- 右侧主要内容区 -->
    <el-container>
      <!-- 顶部栏 -->
      <el-header class="app-header">
        <div class="header-left">
          <h2>城市交通信号智能优化平台</h2>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" :icon="UserFilled" />
              <span class="username">交警支队</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 页面主体 -->
      <el-main class="app-main">
        <router-view v-slot="{ Component }">
          <keep-alive>
            <component :is="Component" />
          </keep-alive>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Odometer,
  Location,
  DataLine,
  Timer,
  PieChart,
  Fold,
  UserFilled,
  ArrowDown
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const isCollapse = ref(false) // 菜单折叠状态

// 当前激活的菜单项
const activeMenu = computed(() => route.path)

// 折叠/展开菜单
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

// 下拉菜单命令
const handleCommand = (command) => {
  if (command === 'logout') {
    localStorage.removeItem('token')
    ElMessage.success('已退出登录')
    router.push('/login')
  }
}
</script>

<style scoped>
/* 整个应用容器 */
.app-container {
  height: 100vh;
  background: #0a0f1e; /* 深色科技风背景 */
}

/* 左侧菜单栏样式 */
.app-aside {
  background: #101624;
  border-right: 1px solid #1f2a3e;
  transition: width 0.3s ease;
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.3);
}

.logo-box {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #00d4ff;
  font-weight: bold;
  font-size: 18px;
  border-bottom: 1px solid #1f2a3e;
  margin-bottom: 20px;
}

.logo-text {
  letter-spacing: 2px;
}

.logo-icon {
  font-size: 28px;
}

.el-menu-vertical {
  border-right: none;
  background: transparent;
  flex: 1;
}

.el-menu-vertical:not(.el-menu--collapse) {
  width: 100%;
}

.collapse-btn {
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #bfcbd9;
  cursor: pointer;
  border-top: 1px solid #1f2a3e;
  transition: all 0.3s;
}

.collapse-btn:hover {
  color: #00d4ff;
}

/* 顶部栏样式 */
.app-header {
  background: #101624;
  border-bottom: 1px solid #1f2a3e;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

.header-left h2 {
  font-size: 20px;
  font-weight: 500;
  color: #eef2ff;
  margin: 0;
  letter-spacing: 1px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #eef2ff;
}

.username {
  font-size: 14px;
}

/* 主体内容区 */
.app-main {
  background: #0a0f1e;
  padding: 20px;
  overflow-y: auto;
  color: #eef2ff;
}

/* 滚动条样式（可选） */
.app-main::-webkit-scrollbar {
  width: 6px;
}
.app-main::-webkit-scrollbar-track {
  background: #1f2a3e;
}
.app-main::-webkit-scrollbar-thumb {
  background: #00d4ff;
  border-radius: 3px;
}
</style>