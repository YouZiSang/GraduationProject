package com.example.yzs.service.exchange;

import com.example.yzs.entity.exchange.Comment;
import com.example.yzs.entity.exchange.Community;

import java.util.List;

public interface CommunityService {
    void addArticles(Community community);

    List<Community> findAllArticles();

    void thumbsUp(Comment comment);

    List<Comment> listThumbsUp();

    List<Comment> selectAllComments();

    void addComments(Comment comment);

    void cancelThumbUp(Comment comment);

    void deleteArticle(Community community);

    void deleteComment(Comment comment);
}
