import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/manager/home' },
    {
      path: '/manager',
      component: () => import('@/views/Manager.vue'),
      children: [
        { path: 'home', meta: { name: '系统首页' }, component: () => import('@/views/manager/Home.vue'),  },
        { path: 'admin', meta: { name: '管理员信息' }, component: () => import('@/views/manager/Admin.vue'), },
        { path: 'notice', meta: { name: '系统公告' }, component: () => import('@/views/manager/Notice.vue'), },
        { path: 'person', meta: { name: '个人资料' }, component: () => import('@/views/manager/Person.vue'), },
        { path: 'password', meta: { name: '修改密码' }, component: () => import('@/views/manager/Password.vue'), },
        { path: 'teacher', meta: { name: '教师信息' }, component: () => import('@/views/manager/Teacher.vue'), },
        { path: 'project', meta: { name: '科研项目' }, component: () => import('@/views/manager/Project.vue'), },
        { path: 'process', meta: { name: '科研过程' }, component: () => import('@/views/manager/Process.vue'), },
        { path: 'type', meta: { name: '成果分类' }, component: () => import('@/views/manager/Type.vue'), },
        { path: 'achievement', meta: { name: '科研成果' }, component: () => import('@/views/manager/Achievement.vue'), },
        { path: 'processLine', meta: { name: '科研过程线' }, component: () => import('@/views/manager/ProcessLine.vue'), },
        // 教师反馈——教师端
        { path: 'teacherFeedback', meta: { name: '教师反馈' }, component: () => import('@/views/manager/TeacherFeedback.vue'), },
        // 教师反馈——管理员端
        { path: 'feedback', meta: { name: '教师反馈' }, component: () => import('@/views/manager/Feedback.vue'), },
        { path: 'activity', meta: { name: '学术活动' }, component: () => import('@/views/manager/Activity.vue'), },
        { path: 'teacherActivity', meta: { name: '学术活动' }, component: () => import('@/views/manager/TeacherActivity.vue'), },
        { path: 'apply', meta: { name: '我的申请' }, component: () => import('@/views/manager/Apply.vue'), },
        { path: 'log', meta: { name: '日志管理' }, component: () => import('@/views/manager/Log.vue'), },
        { path: 'dashboard', meta: { name: '数据统计' }, component: () => import('@/views/manager/Dashboard.vue'), },

      ]
    },
    {
      path: '/front',
      component: () => import('@/views/Front.vue'),
      children: [
        { path: 'home', component: () => import('@/views/front/Home.vue'),  },
        { path: 'person', component: () => import('@/views/front/Person.vue'),  }
      ]
    },
    { path: '/login', component: () => import('@/views/Login.vue') },
    { path: '/register', component: () => import('@/views/Register.vue') },
    { path: '/404', component: () => import('@/views/404.vue') },
    { path: '/:pathMatch(.*)', redirect: '/404' }
  ]
})

export default router
