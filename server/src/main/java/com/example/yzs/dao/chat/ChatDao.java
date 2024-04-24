package com.example.yzs.dao.chat;

import com.example.yzs.entity.chat.Chat;
import com.example.yzs.entity.Questionnaire;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatDao {
    List<Chat> findById(@Param("fromId") Integer fromId, @Param("toId") Integer toId);

    void addSingleChat(Chat chat);

    List<Chat> selectGroupMessage();

    void addGroupChat(Chat chat);

    List<Questionnaire> aa();
    List<Questionnaire> bb();

    List<Chat> unReaded();

    void updateIsReade(Chat chat);
}
