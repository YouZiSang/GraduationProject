import request from "@/utils/request.js";

export const getChatMessageService = (fromId,toId)=>{
    return request.get('/chat/single?fromId=' + fromId +'&toId=' + toId)
}

export const groupChatHistoryService = ()=>{
    return request.get('/chat/group')
}

export const isReadedService = ()=>{
    return request.get('/chat/isReaded')
}


export const updateReadedService = (params)=>{
    return request.post('/chat/updateReaded/',params)
}