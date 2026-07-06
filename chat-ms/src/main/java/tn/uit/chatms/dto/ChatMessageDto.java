package tn.uit.chatms.dto;

public record ChatMessageDto(
    Long conversationId,
    String senderId,
    String content,
    String fileUrl,
    String messageType
) {}