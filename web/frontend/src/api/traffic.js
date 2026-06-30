import request from '@/utils/request'

// 1. 获取测试数据集（5个路口）
export const getTestData = () => {
  return request.get('/api/traffic/data/test')
}

// 2. 单路口优化
export const optimizeSingle = (data) => {
  return request.post('/api/traffic/optimize/single', data)
}

// 3. 批量优化
export const optimizeBatch = (data) => {
  return request.post('/api/traffic/optimize/batch', data)
}