package com.example.yzs.component;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.example.yzs.config.GetHttpSessionConfig;
import com.example.yzs.entity.ConsultantsInfo;
import com.example.yzs.entity.UserInfo;
import com.example.yzs.entity.chat.Chat;
import com.example.yzs.service.ConsultantsInfoService;
import com.example.yzs.service.UserInfoService;
import com.example.yzs.service.chat.ChatService;
import com.example.yzs.utils.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat/{name}/{avatar}", configurator= GetHttpSessionConfig.class)
@Component
public class WebSocketChatEndpoint {

    private static final Map<Integer, WebSocketChatEndpoint> onlineUsers = new ConcurrentHashMap<>();
    private static final Map<Integer,Object> userMap = new ConcurrentHashMap<>();

    private static ChatService groupChatService;

    private static UserInfoService userInfoService;

    private static ConsultantsInfoService consultantsInfoService;
    @Autowired
    private void setDeviceListenerService(ChatService groupChatService,UserInfoService userInfoService,ConsultantsInfoService consultantsInfoService) {
        WebSocketChatEndpoint.groupChatService = groupChatService;
        WebSocketChatEndpoint.userInfoService = userInfoService;
        WebSocketChatEndpoint.consultantsInfoService = consultantsInfoService;
    }


    private HttpSession httpSession;
    private Session session;
    /**
     * 建立websocket连接，被调用
     * @param session
     */

    @OnOpen
    public void onOpen(Session session, EndpointConfig config,
                       @PathParam("name")String name,@PathParam("avatar")String avatar){

        //1.将局部的session对象赋值给成员session
        this.session = session;
        //2，将session进行保存
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());

        //从httpSeesion对象中获取用户名
        Integer accountId = (Integer) httpSession.getAttribute("uid");
        if (accountId == null){
            accountId = (Integer) httpSession.getAttribute("cid");
        }
        onlineUsers.put(accountId, this);

        Dict user = Dict.create().set("name", URLUtil.decode(Base64.decodeStr(name))).set("avatar", URLUtil.decode(Base64.decodeStr(avatar)));
            userMap.put(accountId, user);

        //3.广播消息：需要将登录所有的用户推送给所有的用户
        broadcastAllUser(JSONUtil.toJsonStr(Dict.create().set("user", userMap).set("messageType","onlineUsers")));

    }

    public Set<Integer> getFriends(){
        return onlineUsers.keySet();
    }

    public void broadcastAllUser(String message) {
        try {
            //遍历map集合
            Set<Integer> users = onlineUsers.keySet();
            for (Integer id : users){
                //获取所有用户对应的session对象
                WebSocketChatEndpoint chatEndpoint  = onlineUsers.get(id);
                //发送消息
                chatEndpoint.session.getBasicRemote().sendText(message); // 发送消息和 JSON 数据
            }

        }catch (Exception e){
            //记录日志
            e.printStackTrace();
        }


    }

    /**
     * 浏览器发送消息到服务器，该方法被调用
     * @param message
     */
    @OnMessage
    public void onMessage(String message,Session session){
        System.out.println(message);
        try {
            ObjectMapper mapper = new ObjectMapper();
            Chat chat = mapper.readValue(message, Chat.class);
            groupChatService.addGroupChat(chat);
            broadcastAllUser(message);
//
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 断开websocket时被调用
     * @param session
     */
    @OnClose
    public void onClose(Session session){
        //1.从onlineUsers中删除当前用户的session对象
        Integer userId = (Integer) this.httpSession.getAttribute("uid");
        System.out.println(userId);
        if (userId == null){
            Integer consultantsId = (Integer) this.httpSession.getAttribute("cid");
            onlineUsers.remove(consultantsId);
        }else {
            onlineUsers.remove(userId);
        }


        //2,通知其他所有用户，当前用户下线了
        String message = MessageUtils.getMessage(true,null,getFriends());
//        broadcastAllUser(message);
    }

}
