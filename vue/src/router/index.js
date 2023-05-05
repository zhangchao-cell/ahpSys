import Vue from 'vue'
import VueRouter from 'vue-router'
import HelloWorld from '../components/HelloWorld'
import T5 from '../components/T5'
import ruleSingleResult from '../components/ruleSingleResult'
import schemeSingleResultDetail from '../components/schemeSingleResultDetail'
import schemeSingleResult from '../components/schemeSingleResult'
import Home from '../views/Home'
import BuildModel from '../views/BuildModel'
import RuleWeightResult from '../views/RuleWeightResult'
import SchemeWeightResult from '../views/SchemeWeightResult'
import Logon from '../views/logon'
import Register from '../views/register'
import PersonInfo from '../views/personInfo'


Vue.use(VueRouter)

export default new VueRouter({
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
      redirect: 'buildModel',
      children: [
        {
          path: 'buildModel',
          name: 'BuildModel',
          component: BuildModel,
        },
        {
          path: 'ruleWeightResult',
          name: 'RuleWeightResult',
          component: RuleWeightResult,
        },
        {
          path: 'schemeWeightResult',
          name: 'SchemeWeightResult',
          component: SchemeWeightResult,
        },
        {
          path: '/personInfo',
          name: 'PersonInfo',
          component: PersonInfo,
        },
      ]
    },
    {
      path: '/logon',
      name: 'Logon',
      component: Logon,
    },
    {
      path: '/register',
      name: 'Register',
      component: Register,
    },
    {
      path: '/t5',
      component: T5
    },
    {
      path: '/ruleSingleResult',
      name: 'ruleSingleResult',
      component: ruleSingleResult,
    },
    {
      path: '/schemeSingleResultDetail',
      name: 'schemeSingleResultDetail',
      component: schemeSingleResultDetail,
    },
    {
      path: '/schemeSingleResult',
      name: 'schemeSingleResult',
      component: schemeSingleResult,
    },

  ]
})