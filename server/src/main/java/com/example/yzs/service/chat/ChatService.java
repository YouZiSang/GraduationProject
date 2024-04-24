package com.example.yzs.service.chat;

import com.example.yzs.entity.chat.Chat;

import java.util.List;
import java.util.Map;

public interface ChatService {

    List<Chat> findById(Integer fromId, Integer toId);

    void addSingleChat(Chat chat);
    List<Chat> groupMessage();

    void addGroupChat(Chat chat);

    Map<Integer, Long> unReaded();

    void updateReaded(Chat chat,Integer toId);
}
