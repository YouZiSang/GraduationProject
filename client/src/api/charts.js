import request from "@/utils/request.js";

export const chartsBookService = ()=>{
    return request.get('/charts/books',)
}

export const chartsScoreService = ()=>{
    return request.get('/charts/scores',)
}

export const chartSalesService = ()=>{
    return request.get('/charts/sales',)
}

export const chartAppNumberByCidService = ()=>{
    return request.get('/charts/appointmentNumByCid',)
}

export const chartAppNumberService = ()=>{
    return request.get('/charts/appointmentNumber',)
}