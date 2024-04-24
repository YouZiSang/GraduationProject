import request from "@/utils/request.js";

export const adminLoginService = (params) => {
    return request.post('/admin/login?key=' + params.key, params, {
        withCredentials: true
    })
}

export const getUsersListService = (params) => {
    return request.get('/admin/usersList', {params: params})
}

export const deleteUserService = (uid, deleted) => {
    return request.post(`/admin/deleteUser/${uid}/${deleted}`)

}

export const deleteConsultantService = (cid, deleted) => {
    return request.post(`/admin/deleteConsultant/${cid}/${deleted}`)

}
export const adminPersonalService = () => {
    return request.get('/admin/admin/adminPersonalInfo')
}

export const getAllBookService = () => {
    return request.get('/admin/getAllBook')
}


export const addBookInfoService = (addBookInfo) => {

    return request.post('/admin/addBookInfo', addBookInfo)
}

export const updateBookInfoService = (editBookParams) => {

    return request.post('/admin/updateBookInfo', editBookParams)
}

export const delBookInfoService = (delBookInfo) => {

    return request.post('/admin/delBookInfo', delBookInfo)
}

export const searchBookInfoService = (search) => {
    return request.post('/admin/searchBookInfo?search=' + search, {
        headers: {
            'Content-Type': 'application/json'
        },
    })
}

export const searchBookTypeInfoService = (type)=>{
    return request.post('/admin/searchBookTypeInfo?type='+type)
}

export const findAllOrdersService = (pageParams) => {
    return request.get('/admin/findAllOrders',{params: pageParams})
}

export const updateOrderStatusService = (params) => {
    return request.post('/admin/updateOrderStatus',params)
}


export const findBooksTypeService = () => {
    return request.get('/admin/findType')
}

