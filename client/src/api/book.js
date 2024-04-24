import request from "@/utils/request.js";

export const bookListService = (params)=>{
    return request.get('/book/list',{params:params})
}

export const collectionBookService = (selectedBook)=>{
    return request.post('/book/collection',selectedBook)
}

export const updateBooksHouseNumberService = (selectedBook)=>{
    return request.post('/book/updateBooksHouseBooks',selectedBook)
}


export const collectionStatusService = (ISBNs)=>{
    return request.post('/book/findCollectedBooks',ISBNs)
}

export const cancelCollectionService = (ISBN)=>{
    return request.post('/book/deleteCollectedBooks',ISBN, {
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const findBookEvaluationService = (id)=>{
    return request.get(`/book/evaluation?id=${id}`)
}

export const getRelatedBooksService = (type, ISBN) => {
    return request.get(`/book/related?type=${encodeURIComponent(type)}&ISBN=${ISBN}`);
}
export const bookEvaluationService = (params) => {
    return request.post('/book/evaluation', params,{
        headers: {
            'Content-Type': 'application/json', // 确保发送 JSON 对象
        }
    })
}
