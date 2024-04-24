import request from "@/utils/request.js";

export const publishedArticlesService = (params)=>{
    return request.post('/community/addArticles', params)
}

export const selectAllArticlesService = (params)=>{
    return request.get('/community/allArticles', params)
}

export const thumbsUpService = (params)=>{
    return request.post('/community/thumbsUp', params)
}

export const selectAllThumbsUpService = ()=>{
    return request.get('/community/selectAllThumbsUp', )
}

export const addLandlordCommentsService = (params)=>{
    return request.post('/community/addLandlordComments',params )
}
export const addCommentsService = (params)=>{
    return request.post('/community/addComments',params )
}

export const selectAllCommentsService = ()=>{
    return request.get('/community/selectAllComments')
}

export const cancelThumbUpService = (params)=>{
    return request.post('/community/cancelThumbUp',params )
}

export const delArticleService = (params)=>{
    return request.post('/community/delArticle',params )
}

export const delCommentService = (params)=>{
    return request.post('/community/delComment',params )
}