package com.example.yzs.controller.chat;

import com.example.yzs.constant.Result;
import com.example.yzs.entity.chat.Chat;
import com.example.yzs.service.chat.ChatService;
import com.example.yzs.utils.ThreadLocalUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "chat")
public class ChatController {

    @Resource
    private ChatService chatService;

    @GetMapping("/single")
    public Result<List<Chat>> findByFromId(@RequestParam Integer fromId,@RequestParam Integer toId){
        List<Chat> chats = chatService.findById(fromId,toId);
        return Result.success(chats);
    }

//    @GetMapping("/selectOnlineUser")
//    public Result<List<Chat>> selectOnlineUser

    @GetMapping("/group")
    public Result<List<Chat>> groupMessageResult(){
        List<Chat> chats = chatService.groupMessage();
        return Result.success(chats);
    }

    @GetMapping("/isReaded")
    public Result<Map<Integer, Long>> unReaded() {
        return Result.success(chatService.unReaded());
    }

    @PostMapping("updateReaded")
    public Result<String> isReaded(@RequestBody Chat chat){
        Map<Integer,Object> map = ThreadLocalUtil.get();
        Integer toId = map.get("uid") != null ? (Integer) map.get("uid") : (Integer) map.get("cid");
        chatService.updateReaded(chat,toId);
        return Result.success("已读");
    }


}
