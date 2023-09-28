import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login.vue'
import ProductList from '@/components/ProductList.vue'
import ProductDetail from '@/components/ProductDetail.vue'
import CategoryList from '@/components/CategoryList.vue'
import CategoryDetail from '@/components/CategoryDetail.vue'
import UserList from '@/components/UserList.vue'
import UserDetail from '@/components/UserDetail.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login
    },
    {
      path: '/products',
      name: 'ProductList',
      component: ProductList,
      meta: { requiresAuth: true }
    },
    {
      path: '/products/:id',
      name: 'ProductDetail',
      component: ProductDetail,
      props: true,
      meta: { requiresAuth: true }
    },
    {
      path: '/categories',
      name: 'CategoryList',
      component: CategoryList,
      meta: { requiresAuth: true }
    },
    {
      path: '/categories/:id',
      name: 'CategoryDetail',
      component: CategoryDetail,
      props: true,
      meta: { requiresAuth: true }
    },
    {
      path: '/users',
      name: 'UserList',
      component: UserList,
      meta: { requiresAuth: true }
    },
    {
      path: '/users/:id',
      name: 'UserDetail',
      component: UserDetail,
      props: true,
      meta: { requiresAuth: true }
    }
  ]
})
