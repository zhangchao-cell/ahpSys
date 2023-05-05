import Vue from 'vue'
import App from './App.vue'
import API from './api/axios'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import router from '@/router/index.js'
import request from "@/utils/request";

Vue.config.productionTip = false
Vue.prototype.$API = API

Vue.prototype.$bus = new Vue()
Vue.prototype.request = request;

Vue.use(ElementUI)
new Vue({
  render: h => h(App),
  router
}).$mount('#app')
