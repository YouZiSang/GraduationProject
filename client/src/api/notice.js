import request from "@/utils/request.js";

export const addNoticeService = (params) => {
    return request.post('/notice/add',params)
}

export const getAllNoticeService = () => {
    return request.get('/notice/getAll')
}

export const editNoticeService = (params) => {
    return request.post('/notice/edit',params)
}

export const delNoticeService = (id) => {
    return request.post(`/notice/edit/${id}`);
}

export const getCarouselImgService = () => {
    return request.get(`/notice/carouselImg`);
}

export const editCarouselImgService = (params) => {
    return request.post(`/notice/editCarousel`,params);
}

export const getEvaluationHighService = (params) => {
    return request.get(`/notice/settle`,params);
}