package tn.uit.chatms.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import tn.uit.chatms.dto.CallInviteDto;
import tn.uit.chatms.dto.ChatMessageDto;
import tn.uit.chatms.entity.Message;
import tn.uit.chatms.service.ChatService;

import java.util.Map;

@Controller
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessageDto dto) {
        Message saved = chatService.saveMessage(dto);
        Map<String, Object> response = Map.of(
                "id", saved.getId(),
                "conversationId", dto.getConversationId(),
                "senderId", saved.getSender().getUsername(),
                "content", saved.getContent(),
                "timestamp", saved.getTimestamp(),
                "status", saved.getStatus().name()
        );
        messagingTemplate.convertAndSend("/topic/chat/" + dto.getConversationId(), response);
    }

    @MessageMapping("/call.invite")
    public void inviteToCall(CallInviteDto dto) {
        messagingTemplate.convertAndSend("/topic/call/" + dto.getToUserId(), dto);
    }
}
