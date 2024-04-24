import request from "@/utils/request.js";

export const addToCartService = (selectedBook)=>{
    return request.post('/shoppingCats/addBooks',selectedBook)
}


export const selectShoppingCartService = (shopperId)=>{
    return request.get(`/shoppingCats/selectBooks?id=${shopperId}`)
}

export const batchDeleteShoppingCartService = (shopperId,selectedItems)=>{
    return request.post(`/shoppingCats/batchDeleteBooks?id=${shopperId}`,selectedItems)
}

export const deleteShoppingCartService = (shopperId, ISBN) => {
    const params = new URLSearchParams();
    params.append('shopperId', shopperId);
    params.append('ISBN', ISBN);
    return request.post('/shoppingCats/deleteBooks?' + params.toString());
}

export const shoppingNumberAndTotalService = ()=>{
    return request.get('/shoppingCats/selectBooksNumber')
}

export const updateNumberService = (selectedBook)=>{
    return request.post('/shoppingCats/updateNumber',selectedBook)
}

export const buyBooksService = (shopperId,selectedItems)=>{
    return request.post(`/orders/buyBooks?id=${shopperId}`,selectedItems)
}

