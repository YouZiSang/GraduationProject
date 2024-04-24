import request from "@/utils/request.js";

export const uploadSingleFile = (file)=>{
    return request.post('/upload/singleFile',file,{
        headers:{
            "Content-Type":"multipart/form-data"
        }
    })
}


export const multiFileUpload = (params) => {
    return request.post('/upload/multiFile', params, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}