//导入request.js请求工具
import request from '@/utils/request.js'

//提供调用登录接口的函数
export const userLoginService = (params)=>{
    return request.post('/user/login?key=' + params.key,params,{
        withCredentials: true
    })
}
export const updatePassword = (update)=>{
    return request.post('/user/updatePassword',update)
}

export const getAppointmentTable = (cid)=>{
    return request.get(`user/appointmentTable?cid=${cid}`)
}

export const appointmentService = (params)=>{
    return request.post('/user/appointment', params)
}

export const findUserAppointmentInfo = ()=>{
    return request.get('/user/appointmentInfo')
}

export const queInfo = ()=>{
    return request.get('/question')
}
