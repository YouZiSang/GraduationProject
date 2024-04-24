package com.example.yzs.component;

import cn.hutool.json.JSONUtil;
import com.example.yzs.config.GetHttpSessionConfig;
import com.example.yzs.entity.chat.Chat;
import com.example.yzs.service.ConsultantsInfoService;
import com.example.yzs.service.UserInfoService;
import com.example.yzs.service.chat.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/videoChat", configurator = GetHttpSessionConfig.class)
@Component
public class WebRTC {
    private static final Map<Integer, WebRTC> onlineUsers = new ConcurrentHashMap<>();
    private static final Map<Integer,Object> userMap = new ConcurrentHashMap<>();
    private HttpSession httpSession;
    private Session session;
    private Integer userId;
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        //2，将session进行保存
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
         userId = (Integer) httpSession.getAttribute("uid");
        if (userId == null) {
            userId = (Integer) httpSession.getAttribute("cid");
            onlineUsers.put(userId, this);
        } else {
            onlineUsers.put(userId, this);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            // 解析 JSON 消息为 JsonNode 对象
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            // 检查消息中是否包含 type 字段
            if (jsonNode.has("type")) {
                String messageType = jsonNode.get("type").asText();

                // 根据消息类型进行处理
                switch (messageType) {
                    case "offer":
                        handleOffer(jsonNode, session);
                        break;
                    case "answer":
                        handleAnswer(jsonNode, session);
                        break;
                    case "candidate":
                        handleCandidate(jsonNode, session);
                        break;
                    default:
                        System.out.println("Unsupported message type: " + messageType);
                        break;
                }
            } else {
                System.out.println("Message type not found in message: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleOffer(JsonNode offerMessage, Session session) throws IOException {
        // 处理 offer 消息
        Integer toId = offerMessage.get("toId").asInt();
        WebRTC toUserEndpoint = onlineUsers.get(toId);
        if (toUserEndpoint != null) {
            toUserEndpoint.session.getBasicRemote().sendText(offerMessage.toString());
        } else {
            System.out.println("Recipient is not online");
        }
    }

    private void handleAnswer(JsonNode answerMessage, Session session) throws IOException {
        // 处理 answer 消息
        Integer fromId = answerMessage.get("fromId").asInt();
        WebRTC fromUserEndpoint = onlineUsers.get(fromId);
        if (fromUserEndpoint != null) {
            fromUserEndpoint.session.getBasicRemote().sendText(answerMessage.toString());
        } else {
            System.out.println("Sender is not online");
        }
    }

    private void handleCandidate(JsonNode candidateMessage, Session session) throws IOException {
        // 处理 candidate 消息
        Integer toId = candidateMessage.get("toId").asInt();
        WebRTC endpoint = onlineUsers.get(toId);
        if (endpoint != null) {
            endpoint.session.getBasicRemote().sendText(candidateMessage.toString());
        } else {
            System.out.println("Target user is not online");
        }
    }

    @OnClose
    public void onClose(Session session) {
        onlineUsers.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}
