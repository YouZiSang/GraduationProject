import request from "@/utils/request.js";

export const addConsultationService = (params)=>{
    return request.post('/consultation/add',params)
}

export const findConsultationTableService = ()=>{
    return request.get('/consultation/table')
}

export const updateConsultationStatusService = (params)=>{
    return request.post('/consultation/status',params)
}


export const consultationEvaluationService = (params) => {
    return request.post('/consultation/evaluation', params,{
        headers: {
            'Content-Type': 'application/json', // 确保发送 JSON 对象
        }
    })
}