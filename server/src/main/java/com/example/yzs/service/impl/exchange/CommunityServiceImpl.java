package com.example.yzs.service.impl.exchange;

import com.example.yzs.dao.exchange.CommunityDao;
import com.example.yzs.entity.exchange.Comment;
import com.example.yzs.entity.exchange.Community;
import com.example.yzs.service.exchange.CommunityService;
import com.example.yzs.utils.ThreadLocalUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommunityServiceImpl implements CommunityService {

    private final CommunityDao communityDao;

    public CommunityServiceImpl(CommunityDao communityDao) {
        this.communityDao = communityDao;
    }

    //发帖
    @Override
    public void addArticles(Community community) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer uid = (Integer) map.get("uid");
        Integer cid = (Integer) map.get("cid");

        if (uid == null) {

            community.setRole("咨询师");
        } else {
            community.setRole("用户");
        }
        communityDao.addArticles(community);
    }

    @Override
    public List<Community> findAllArticles() {
        return communityDao.selectAllArticles();
    }

    //点赞
    @Override
    public void thumbsUp(Comment comment) {
        System.out.println(comment);
        //添加评论到评论表
        communityDao.addThumbsUp(comment);

        if (comment.getCommentReplyId() != null){

            communityDao.updateCommentThumbsUp(comment);
        }else {

            Integer landlordId = comment.getLandlordId();
            //查点赞数
            Community community = communityDao.selectLike(landlordId);
            //点赞数加一
            communityDao.addThumbsUpLike(comment);
            //修改评论
            communityDao.updateMessages(community);

        }
    }

    @Override
    public List<Comment> listThumbsUp() {
        return communityDao.selectAllThumbsUp();
    }

    @Override
    public void addComments(Comment comment) {
        System.out.println(comment);
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("uid");
        if (comment.getCommentId().equals(userId)) {
            comment.setCommentId(userId);
            comment.setRole("用户");
        } else {
            Integer consultantsId = (Integer) map.get("cid");
            comment.setCommentId(consultantsId);
            comment.setRole("咨询师");
        }

        communityDao.addComments(comment);
        communityDao.addCommentsToMessages(comment.getLandlordId());
        if (comment.getReplyId() !=null ){
            communityDao.updateCommentNumber(comment);
        }
    }

    @Override
    public void cancelThumbUp(Comment comment) {
        if (comment.getCommentReplyId() != null){
            communityDao.deleteCommentThumbsUp(comment);
            communityDao.deleteCommentThumbsUpLike(comment);
        }else {
            communityDao.deleteThumbsUp(comment);
            communityDao.deleteThumbsUpLike(comment);
        }


    }

    @Override
    public void deleteArticle(Community community) {
        communityDao.delArticleAllComment(community);
        communityDao.deleteArticle(community);
    }

    @Override
    public void deleteComment(Comment comment) {
        communityDao.deleteComment(comment);
    }

    @Override
    public List<Comment> selectAllComments() {

        return communityDao.selectAllComment();
    }

}
