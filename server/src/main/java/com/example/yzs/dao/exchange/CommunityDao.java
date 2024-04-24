package com.example.yzs.dao.exchange;

import com.example.yzs.entity.exchange.Comment;
import com.example.yzs.entity.exchange.Community;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunityDao {
    void addArticles(Community community);

    List<Community> selectAllArticles();

    Community selectLike(Integer landlordId);

    void updateMessages(Community community);

    void addThumbsUp(Comment comment);

    List<Comment> selectAllThumbsUp();

    void addCommentsToMessages(Integer landLordId);

    List<Comment> selectAllComment();

    void updateCommentThumbsUp(Comment comment);

    void addComments(Comment comment);

    void updateCommentNumber(Comment comment);

    void deleteThumbsUp(Comment comment);

    void deleteThumbsUpLike(Comment comment);

    void deleteCommentThumbsUp(Comment comment);

    void deleteCommentThumbsUpLike(Comment comment);

    void addThumbsUpLike(Comment comment);

    int selectThumbsUp(Comment comment);

    void deleteArticle(Community community);

    void delArticleAllComment(Community community);

    void deleteComment(Comment comment);
}
