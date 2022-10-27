import Vue from 'vue'
import VueRouter from 'vue-router'
import multiguard from 'vue-router-multiguard';
import { store } from "@/store";
import { Auth } from 'aws-amplify';

import Home from '../views/Home.vue'


Vue.use(VueRouter)

const loggedIn = (from, to, next) => {
    if(!store.getters["user/isLoggedIn"]) {
        Auth.currentSession().then(res => {
            console.log(res);
            store.commit("user/loginSuccess", {
              token: res.getIdToken().getJwtToken(),
              role: res.getIdToken().decodePayload()["cognito:groups"]
            });

            next()
          })
          .catch(() => {
              next('/auth/login')
          });
    }

    next()
}

const loggedOut = (from, to, next) => {
    if(store.getters["user/isLoggedIn"]) {
       next('/')
    }
    if(!store.getters["user/isLoggedIn"]) {
        Auth.currentSession().then(res => {
            store.commit("user/loginSuccess", {
              token: res.getIdToken().getJwtToken(),
              role: res.getIdToken().decodePayload()["cognito:groups"]
            });

            next('/')
          })
          .catch(() => {
              next()
          });
    }
}

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    beforeEnter: multiguard([loggedIn])
  },
  {
    path: '/auth/login',
    name: 'Login',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "login" */ '../views/auth/Login'),
    beforeEnter: multiguard([loggedOut]),
    meta: {
        layout: "no-sidebar"
    }
  },
    {
    path: '/auth/register',
    name: 'Register',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "login" */ '../views/auth/Register'),
    beforeEnter: multiguard([loggedOut]),
    meta: {
        layout: "no-sidebar"
    }
  },
    {
    path: '/auth/confirm',
    name: 'Confirm',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "login" */ '../views/auth/Confirm'),
    beforeEnter: multiguard([loggedOut]),
    meta: {
        layout: "no-sidebar"
    }
  },
    {
    path: '/subscription',
    name: 'subscription',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "login" */ '../views/Subscription'),
    beforeEnter: multiguard([loggedIn])
  },
    {
    path: '/subscription/check',
    name: 'subscription-check',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "login" */ '../views/SubscriptionCheck'),
    beforeEnter: multiguard([loggedIn])
  },
    {
    path: '/category/:id',
    name: 'category',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "login" */ '../views/Category'),
    beforeEnter: multiguard([loggedIn])
  },

  // User Routes
    {
        path: '/my-music',
        name: 'Music',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "login" */ '../views/user/Music'),
        beforeEnter: multiguard([loggedIn])
    },

  // Admin Routes
    {
    path: '/admin/categories',
    name: 'adminCategories',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "login" */ '../views/administrator/Categories'),
    beforeEnter: multiguard([loggedIn])
  },
    {
    path: '/admin/monitoring',
    name: 'adminMonitoring',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "login" */ '../views/administrator/Monitoring'),
    beforeEnter: multiguard([loggedIn])
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
