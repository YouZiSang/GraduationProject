package com.example.yzs.controller.exchange;

import com.example.yzs.constant.MessageConstant;
import com.example.yzs.constant.Result;
import com.example.yzs.entity.exchange.Comment;
import com.example.yzs.entity.exchange.Community;
import com.example.yzs.service.exchange.CommunityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community")
public class CommunityController {

    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @PostMapping("/addArticles")
    public Result<Community> result(@RequestBody Community community) {
        communityService.addArticles(community);
        return Result.success(MessageConstant.PUBLISHED_SUCCESS,community);
    }

    //获取全部文章（帖子）
    @GetMapping("/allArticles")
    public Result<List<Community>> listResult() {
        return Result.success(communityService.findAllArticles());
    }

    //点赞
    @PostMapping("/thumbsUp")
    public Result<String> thumbsUp(@RequestBody Comment comment) {
        communityService.thumbsUp(comment);
        return Result.success(MessageConstant.THUMBS_UP_SUCCESS);
    }

    @PostMapping("cancelThumbUp")
    public Result<String> cancelThumbUp(@RequestBody Comment comment) {
        communityService.cancelThumbUp(comment);
        return Result.success(MessageConstant.CANCEL_THUMB_UP_SUCCESS);
    }

    //全部点赞
    @GetMapping("/selectAllThumbsUp")
    public Result<List<Comment>> listThumbsUp(){
        List<Comment> comments = communityService.listThumbsUp();
        return Result.success(comments);
    }

    //添加楼主评论
    @PostMapping("/addLandlordComments")
    public Result<String> addLandlordComments(@RequestBody Comment comment) {
        communityService.addComments(comment);
        return Result.success(MessageConstant.COMMENT_SUCCESS);
    }

    //添加评论
    @PostMapping("/addComments")
    public Result<String> addComments(@RequestBody Comment comment) {
        communityService.addComments(comment);
        return Result.success(MessageConstant.COMMENT_SUCCESS);
    }
    //查看所有评论
    @GetMapping("/selectAllComments")
    public Result<List<Comment>> selectAllComments() {
        List<Comment> comments = communityService.selectAllComments();
        return Result.success(comments);
    }

    @PostMapping("/delArticle")
    public Result<String> deleteArticle(@RequestBody Community community) {
        communityService.deleteArticle(community);
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

    @PostMapping("/delComment")
    public Result<String> deleteComment(@RequestBody Comment comment) {
        communityService.deleteComment(comment);
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }
}
