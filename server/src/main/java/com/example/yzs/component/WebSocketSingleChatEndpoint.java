package com.example.yzs.component;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.example.yzs.config.GetHttpSessionConfig;
import com.example.yzs.entity.chat.Chat;
import com.example.yzs.service.chat.ChatService;
import com.example.yzs.utils.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/singleChat", configurator = GetHttpSessionConfig.class)
@Component
public class WebSocketSingleChatEndpoint {

    private static final Map<Integer, WebSocketSingleChatEndpoint> onlineUsers = new ConcurrentHashMap<>();
    static ChatService singleChatService;

    @Autowired
    public void setDeviceListenerService(ChatService singleChatService) {
        WebSocketSingleChatEndpoint.singleChatService = singleChatService;
    }

    private HttpSession httpSession;
    private Session session;

    /**
     * 建立websocket连接，被调用
     *
     * @param session
     */
    private Integer userId;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {

        //1.将局部的session对象赋值给成员session
        this.session = session;
        //2，将session进行保存
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());

        //从httpSession对象中获取用户名
        userId = (Integer) httpSession.getAttribute("uid");
        if (userId == null) {
            userId = (Integer) httpSession.getAttribute("cid");
            onlineUsers.put(userId, this);
        } else {
            onlineUsers.put(userId, this);
        }

        broadcastUserOnline(JSONUtil.toJsonStr(Dict.create().set("id", getFriends()).set("messageType", "online")));
        // 广播新用户上线的消息，只包含该用户的信息
    }

    public Set<Integer> getFriends() {
        return onlineUsers.keySet();
    }

    public void broadcastUserOnline(String message) {
        try {
            // 将新用户信息转换为 JSON 字符串

            // 遍历map集合，但不包括当前用户
            for (Map.Entry<Integer, WebSocketSingleChatEndpoint> entry : onlineUsers.entrySet()) {
                Integer id = entry.getKey();
                WebSocketSingleChatEndpoint chatEndpoint = entry.getValue();

                // 避免向自己发送上线消息
                // 发送消息
                chatEndpoint.session.getBasicRemote().sendText(message);

            }
        } catch (Exception e) {
            // 记录日志
            e.printStackTrace();
        }
    }

    /**
     * 浏览器发送消息到服务器，该方法被调用
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Chat chat = mapper.readValue(message, Chat.class);
            singleChatService.addSingleChat(chat);

            //将消息推送给指定的用户
            Integer toId = chat.getToId();
            Integer fromId = chat.getFromId();

            String content = JSONUtil.toJsonStr(chat);
            //获取推送给指定用户的消息格式数据
            String resultMessage = String.valueOf(content);
            // 发送数据
            WebSocketSingleChatEndpoint toUserEndpoint = onlineUsers.get(toId);
            if (toUserEndpoint != null) {
                toUserEndpoint.session.getBasicRemote().sendText(resultMessage);
            } else {
                System.out.println("不在线");
            }

            WebSocketSingleChatEndpoint fromUserEndpoint = onlineUsers.get(fromId);
            if (fromUserEndpoint != null) {
                fromUserEndpoint.session.getBasicRemote().sendText(resultMessage);
            } else {
                System.out.println("不在线");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 断开websocket时被调用
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        //1.从onlineUsers中删除当前用户的session对象
        Integer userId = (Integer) this.httpSession.getAttribute("uid");
        if (userId == null) {
            Integer consultantsId = (Integer) this.httpSession.getAttribute("cid");
            onlineUsers.remove(consultantsId);
        } else {
            onlineUsers.remove(userId);
        }
    }

}
