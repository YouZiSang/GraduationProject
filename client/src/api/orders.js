import request from "@/utils/request.js";

export const ordersListService = (params) => {
    return request.get('/orders/selectOrders', {params: params})
}

export const updateStatusService = (params) => {
    return request.post('/orders/updateStatus', params)
}

export const evaluationImgService = (params) => {
    return request.post('/upload/multiFile', params, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

export const orderEvaluationService = (params) => {
    return request.post('/orders/evaluation', params,{
        headers: {
            'Content-Type': 'application/json', // 确保发送 JSON 对象
        }
    })
}

export const delOrderService = (orderNo) => {

    return request.post(`/orders/delOrder?orderNo=${orderNo}`, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
}