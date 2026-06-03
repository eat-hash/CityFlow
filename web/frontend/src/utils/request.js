import axios from 'axios'

const request = axios.create({
  baseURL: 'http://localhost:8081',   // 后端端口8081
  timeout: 10000
})

// 请求拦截器（可选，加token）
request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截器：直接返回data部分（后端返回格式 {code, msg, data}）
request.interceptors.response.use(
  response => {
    if (response.data.code === 200) {
      return response.data.data
    } else {
      ElMessage.error(response.data.msg || '请求失败')
      return Promise.reject(response.data)
    }
  },
  error => {
    ElMessage.error('网络错误，请稍后重试')
    return Promise.reject(error)
  }
)

export default request