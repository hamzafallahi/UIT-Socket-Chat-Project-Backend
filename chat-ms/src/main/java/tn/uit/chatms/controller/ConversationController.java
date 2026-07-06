package tn.uit.chatms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.uit.chatms.dto.CreateConversationDto;
import tn.uit.chatms.entity.Conversation;

import tn.uit.chatms.entity.User;
import tn.uit.chatms.service.ChatService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat/conversations")
public class ConversationController {

    private final ChatService chatService;

    public ConversationController(ChatService chatService) {
        this.chatService = chatService;

    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> createConversation(@RequestBody CreateConversationDto dto) {
        Conversation conv = chatService.createConversation(dto);
        return ResponseEntity.ok(toConversationMap(conv));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getConversations(@PathVariable String userId) {
        List<Conversation> convs = chatService.getConversations(userId);
        return ResponseEntity.ok(convs.stream().map(this::toConversationMap).toList());
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Map<String, Object>> getConversation(@PathVariable Long id) {
        Conversation conv = chatService.getConversation(id);
        return ResponseEntity.ok(toConversationMap(conv));
    }





    private Map<String, Object> toConversationMap(Conversation conv) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", conv.getId());
        map.put("type", conv.getType().name());
        map.put("createdAt", conv.getCreatedAt());
        map.put("participants", conv.getUsers().stream().map(User::getUsername).toList());
        return map;
    }
}
