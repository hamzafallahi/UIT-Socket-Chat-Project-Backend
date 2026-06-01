package tn.uit.chatms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.uit.chatms.dto.CreateConversationDto;
import tn.uit.chatms.entity.Conversation;
import tn.uit.chatms.entity.Message;
import tn.uit.chatms.entity.User;
import tn.uit.chatms.repository.UserRepository;
import tn.uit.chatms.service.ChatService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    private final ChatService chatService;
    private final UserRepository userRepository;

    public ChatRestController(ChatService chatService, UserRepository userRepository) {
        this.chatService = chatService;
        this.userRepository = userRepository;
    }

    @PostMapping("/conversations")
    public ResponseEntity<Map<String, Object>> createConversation(@RequestBody CreateConversationDto dto) {
        Conversation conv = chatService.createConversation(dto);
        return ResponseEntity.ok(toConversationMap(conv));
    }

    @GetMapping("/conversations/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getConversations(@PathVariable String userId) {
        List<Conversation> convs = chatService.getConversations(userId);
        return ResponseEntity.ok(convs.stream().map(this::toConversationMap).toList());
    }

    @GetMapping("/conversations/detail/{id}")
    public ResponseEntity<Map<String, Object>> getConversation(@PathVariable Long id) {
        Conversation conv = chatService.getConversation(id);
        return ResponseEntity.ok(toConversationMap(conv));
    }

    @GetMapping("/messages/{conversationId}")
    public ResponseEntity<List<Map<String, Object>>> getMessages(@PathVariable Long conversationId) {
        List<Message> messages = chatService.getMessages(conversationId);
        return ResponseEntity.ok(messages.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", m.getId());
            map.put("conversationId", m.getConversation().getId());
            map.put("senderId", m.getSender() != null ? m.getSender().getUsername() : "unknown");
            map.put("content", m.getContent());
            map.put("timestamp", m.getTimestamp());
            map.put("status", m.getStatus() != null ? m.getStatus().name() : "SENT");
            map.put("fileUrl", m.getFileUrl());
            map.put("messageType", m.getMessageType() != null ? m.getMessageType() : "TEXT"); 

            return map;
        }).toList());
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<Map<String, String>>> searchUsers(@RequestParam String q) {
        List<Map<String, String>> results = userRepository.findByUsernameContainingIgnoreCase(q)
                .stream()
                .map(u -> Map.of(
                        "username", u.getUsername(),
                        "displayName", u.getDisplayName() != null ? u.getDisplayName() : u.getUsername()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
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
