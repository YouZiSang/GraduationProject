import axios from "axios";
//创建一个axios对象
const request = axios.create({
    baseURL: 'http://localhost:8080/api',
    timeout: 5000
})


import {useTokenStore} from '@/stores/token.js'

//添加请求拦截器
request.interceptors.request.use(
    (config) => {
        //请求前的回调
        //添加token
        const tokenStore = useTokenStore();
        //判断有没有token
        console.log(tokenStore.token)
        if (tokenStore.token) {
            config.headers.Authorization = tokenStore.token
        }

        return config;
    },
    (err) => {
        //请求错误的回调
        Promise.reject(err)
    }
)


/* import {useRouter} from 'vue-router'
const router = useRouter(); */

import router from '@/router'
//添加响应拦截器
request.interceptors.response.use(
    res => {
        //判断业务状态码
        if (res.data.code === 0 ) {

            return res.data;
        }
        //操作失败
        ElMessage.error(res.data.message ? res.data.message : '服务异常')
        //异步操作的状态转换为失败
        return Promise.reject(res.data)
    },
    err => {
        //判断响应状态码,如果为401,则证明未登录,提示请登录,并跳转到登录页面
        if (err.response.status === 401) {

            ElMessage.error('请先登录')
            router.push('/login')
        } else {
            ElMessage.error('服务异常')
        }

        return Promise.reject(err);//异步的状态转化成失败的状态
    }
)

export default request;