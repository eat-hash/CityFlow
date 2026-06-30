import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/login', component: () => import('@/views/Login.vue') },
  { path: '/dashboard', component: () => import('@/views/Dashboard.vue'), meta: { requiresAuth: true } },
  { path: '/intersection', component: () => import('@/views/Intersection.vue'), meta: { requiresAuth: true } },
  { path: '/timing', component: () => import('@/views/Timing.vue'), meta: { requiresAuth: true } },
  { path: '/traffic', component: () => import('@/views/Traffic.vue'), meta: { requiresAuth: true } },
  { path: '/visualization', component: () => import('@/views/Visualization.vue'), meta: { requiresAuth: true } },
  { path: '/', redirect: '/dashboard' }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) next('/login')
  else next()
})

export default router