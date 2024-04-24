import request from "@/utils/request.js";

export const consultantsLoginService = (params)=>{
    return request.post('/consultants/login?key=' + params.key,params,{
        withCredentials: true
    })
}

export const appointmentManage = (set)=>{
    return request.post('/consultants/manage', set)
}

export const findAppointment = ()=>{
    return request.get('consultants/findAppointment')
}

export const findConsultantsInfoList = ()=> {
    return request.post('consultants/list')
}
export const consultantPersonalInfoService = () => {
    return request.get('consultants/info')
}

export const editPersonalInfoService = (params) => {
    return request.post('/consultants/updateInfo', params);
};

export const statusEditService = () => {
    return request.get('/consultants/statusEdit');
};

export const getConsultantsEvaluationService = (cid) => {
    return request.get(`/consultants/evaluation?cid=${cid}`);
};