
import request from "@/utils/request";
import {appointmentManage} from "@/api/consultants.js";
export const userPersonalInfoService  = ()=>{
    return request.get('/userInfo/getPersonalInfo')
}

export const editAvatar = (file)=>{
    // const data = new FormData();
    // data.append('file', file);
    // console.log(data)
    return request.post('/upload/singleFile',file,{
        headers:{
            "Content-Type":"multipart/form-data"
        }
    })
}

export const editInfoService = (editInfo)=>{
    return request.post('/userInfo/editPersonalInfo',editInfo)
}

export const getConsultantsInfo = (params)=>{
    return request.get('/consultants/list',{params:params})
}

export const getUsersList = ()=>{
    return request.get('/userInfo/list')
}

export const editfile = (formData) => {
    return request.post('/upload/multiFile', formData);
};

export const upGradeService = (params) => {
    return request.post('/userInfo/upGrade', params);
};

export const statusUpGradeService = () => {
    return request.get('/userInfo/statusUpGrade');
};
