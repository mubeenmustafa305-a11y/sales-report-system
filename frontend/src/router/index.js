import { createRouter, createWebHistory } from 'vue-router'
import ReportsList from '../views/ReportsList.vue'
import ReportDetail from '../views/ReportDetail.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'reports', component: ReportsList },
    { path: '/reports/:id', name: 'report-detail', component: ReportDetail, props: true }
  ]
})

export default router
