package com.example.yzs.utils;

import com.example.yzs.entity.result.ResultMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageUtils {
    public static String getMessage(boolean isSystemMessage, Integer fromId, Object message) {
        try {

            ResultMessage resultMessage = new ResultMessage();
            resultMessage.setSystem(isSystemMessage);
            resultMessage.setMessage(message);
            if (fromId != null) {
                resultMessage.setFromId(fromId);
            }
            ObjectMapper mapper = new ObjectMapper();

            return mapper.writeValueAsString(resultMessage);

        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
