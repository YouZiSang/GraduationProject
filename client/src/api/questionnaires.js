//导入request.js请求工具
import request from '@/utils/request.js'

export const questionnairesService = ()=>{
    return request.get('/questionnaires')
}

export const answerService = (answer)=>{
    return request.post('/answerQuestion',answer)
}

export const surveyCompletedService = ()=>{
    return request.get('/surveyCompleted')
}


export const answerScoreService = (typeScores)=>{
    return request.post('/answerScore',typeScores)
}

