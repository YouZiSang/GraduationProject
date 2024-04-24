package com.example.yzs.service.impl.chat;

import com.example.yzs.dao.chat.ChatDao;
import com.example.yzs.entity.chat.Chat;
import com.example.yzs.service.chat.ChatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class ChatServiceImpl implements ChatService {

    @Resource
    private ChatDao chatDao;

    @Override
    public List<Chat> findById(Integer fromId, Integer toId) {
        List<Chat> list = chatDao.findById(fromId,toId);
        list.forEach(x -> {
            if(x.getToId().equals(fromId) && x.getFromId().equals(toId)){
                x.setReaded(1);
//                chatDao.equals()
            }
        });
        return list;
    }

    @Override
    public void addSingleChat(Chat chat) {
        chatDao.addSingleChat(chat);

    }

    @Override
    public List<Chat> groupMessage() {
        List<Chat> chatList = chatDao.selectGroupMessage();
        return chatList;
    }

    @Override
    public void addGroupChat(Chat chat) {
        chatDao.addGroupChat(chat);
    }

    @Override
    public Map<Integer, Long> unReaded() {
        List<Chat> list = chatDao.unReaded();
        System.out.println(list);
        // 过滤出readed等于1的Chat对象
        List<Chat> filteredList = list.stream()
                .filter(chat -> (chat.getReaded() == 0))
                .collect(Collectors.toList());
        System.out.println(filteredList);
        Map<Integer, Long> unReaded = filteredList.stream()
                .collect(Collectors.groupingBy(Chat::getFromId, Collectors.counting()));
        return unReaded;
    }

    @Override
    public void updateReaded(Chat chat, Integer toId) {
        chat.setToId(toId);
        chatDao.updateIsReade(chat);
    }

}
