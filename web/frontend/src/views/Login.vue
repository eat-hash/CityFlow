<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>城市交通信号智能优化平台</h2>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" style="width: 100%"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="form.password" placeholder="请输入密码" style="width: 100%"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="login" style="width: 100%">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const form = ref({ username: '', password: '' })
const formRef = ref(null)

// 表单校验规则
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

// 预设账号（模拟角色区分）
const users = {
  admin: { password: '123456', role: 'admin', name: '管理员' },
  user: { password: '123456', role: 'user', name: '交警支队' }
}

const login = async () => {
  // 先进行表单校验
  await formRef.value.validate()
  
  const { username, password } = form.value
  const user = users[username]
  
  if (user && user.password === password) {
    // 存储 token（模拟）和用户信息
    localStorage.setItem('token', 'mock-token-' + Date.now())
    localStorage.setItem('userRole', user.role)
    localStorage.setItem('userName', user.name)
    ElMessage.success(`欢迎回来，${user.name}`)
    router.push('/dashboard')
  } else {
    ElMessage.error('用户名或密码错误')
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: #0a0f1e; /* 深色背景，可改为 #f0f2f5 适配浅色 */
}
.login-card {
  width: 400px;
  text-align: center;
  background: rgba(255,255,255,0.95); /* 半透白卡片 */
}
.login-card h2 {
  margin-bottom: 20px;
  color: #333;
}
</style>