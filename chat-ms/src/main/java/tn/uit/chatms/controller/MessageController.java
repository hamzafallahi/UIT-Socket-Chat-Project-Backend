package tn.uit.chatms.controller;

import java.util.HashMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import tn.uit.chatms.entity.Message;
import tn.uit.chatms.service.ChatService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat/messages")

public class MessageController {
        private final ChatService chatService;

        public MessageController(ChatService chatService) {
            this.chatService = chatService;
        }

        @GetMapping("/{conversationId}")
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
}
