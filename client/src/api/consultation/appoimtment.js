import request from "@/utils/request.js";

export const updateAppointmentInfo = (list)=>{
    return request.post('/user/updateAppointmentInfo',list)
}

export const getAppointmentTableService = ()=>{
    return request.get('/getAppTableByCid',)
}

export const getAppointmentNumberService = ()=>{
    return request.get('/getAppointmentNumber',)
}

export const getAppTableByUidService = ()=>{
    return request.get('/getAppTableByUid',)
}

export const updateAppStatusService = (params)=>{
    return request.post('/updateAppStatus',params)
}

export const getAppointmentTypeService = ()=>{
    return request.get('/getAppointmentType')
}