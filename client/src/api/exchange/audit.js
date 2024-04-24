import request from "@/utils/request.js";

export const auditService = ()=>{
    return request.get('/audit')
}

export const consultantsInfoEditService = ()=>{
    return request.get('/audit/consultantsEditInfo')
}

export const auditSuccessUpGradeService = (params)=>{
    return request.post('/audit/successUpGrade',params)
}

export const auditFailUpGradeService = (params)=>{
    return request.post('/audit/failUpGrade',params)
}

export const auditSuccessEditService = (params)=>{
    return request.post('/audit/successEdit',params)
}

export const auditFailEditService = (params)=>{
    return request.post('/audit/failEdit',params)
}


export const upGradeInfoListService = () => {
    return request.get('/audit/upGrade');
};

export const userInfoListService = () => {
    return request.get('/audit/userInfo');
};


