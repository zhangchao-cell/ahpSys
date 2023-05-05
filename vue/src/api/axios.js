import axios from 'axios'

// api 请求列表
const list = {
  getMind: { method: 'get', url: './data.json' },
}


/**
 * name: 解构赋值出 method ， url
 * requireAuth: 请求接口是否需要授权，即是否需要传递 token
 * data: request payload参数   默认{}
 * params: query参数 如?user=jack&age=12   默认{}
 * paths: params参数 如/1/{name}  默认[] ,axios 没有封装 paths参数，需要自己处理
 * headers: headers 请求头 默认 application/json
 * 
 */
const API = (config) => {
  if (list[config.name] === undefined) {
    return Promise.reject(new Error(config.name + '不在list中'))
  }
  const { method, url } = list[config.name]
  const defaultConfig = {
    url,
    method: method.toUpperCase(),
    requireAuth: config.requireAuth ? config.requireAuth : false,
    data: config.data ? config.data : {},
    params: config.params ? config.params : {},
    paths: config.paths ? config.paths : [],
    headers: config.headers ? config.headers : {'Content-Type': 'application/json'}
  }
  // 处理 paths 数据，需要排除 paths 为 [] 的时候，否则无论传不传 paths，defaultConfig.paths 默认为 []
  if (defaultConfig.paths && defaultConfig.paths.constructor === Array) {
    if (defaultConfig.paths.length > 0) {
      defaultConfig.url += '/' + defaultConfig.paths.join('/') // 为 [] 时出来的结果都是 /，导致最后的 url 请求地址末尾多加了 /
    }
  } else {
    return Promise.reject(new Error('paths 数据类型错误'))
  }
  // 处理 params 数据（按情况而定才处理，后端接口如 /udisk/?page=1 就 需要处理）
  // if (Object.keys(defaultConfig.params).length !== 0) {
  //   defaultConfig.url += '/'
  // }
  return new Promise((resolve, reject) => {
    // 使用自定义的 axios 实例请求
    axios(defaultConfig).then(res =>  {
      resolve(res)
    }).catch(error => {
      if (error.status === 200) {
        resolve(error)
      }
      reject(error)
    })
  })
}

// 拦截器
// 添加请求拦截器
axios.interceptors.request.use(req => {
  // 在发送请求之前做什么
  if (req.requireAuth) {
    if (req.headers) {
      // 还要判断前端 token 是否存在
      // 设置 authorization 的值为用户登录成功后返回的 token  js-cookies
    }
  }
  return req
}, error => {
  // 对请求错误做什么
  return Promise.reject(error)
})


// 添加响应拦截器
axios.interceptors.response.use(res => {
  // 对响应数据做什么
  // 处理下载文件的接口
  if (res.config.headers['Content-Type'] === 'application/octet-stream') {
    return res
  }
  if (res.data.code) {
    return Promise.resolve(res)
  } else {
    return Promise.reject(res)
  }
}, error => {
  // 对响应错误做什么
  // if (error.response.status) {
  //   switch (error.response.status) {
  //     case 401:
  //       // 清除 token，未授权错误信息提示，跳转到登录页面
  //       message({ type: 'error', message: '未授权' })
  //       router.replace({
  //         path: '/login',
  //         // 登录成功后跳回浏览的当前页面
  //         query: { redirect: router.currentRoute.fullPath }
  //       })
  //       break
  //     case 500:
  //       // 服务器错误信息提示
  //       message({ type: 'error', message: '服务器错误' })
  //       break
  //     case 404:
  //       // 请求路径错误信息提示
  //       message({ type: 'error', message: '请求路径错误' })
  //       break
  //   }
  // }
  return Promise.reject(error)
})


export default API