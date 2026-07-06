package tn.uit.chatms.dto;

import lombok.Builder;

@Builder // Keep this if you use JitsiRoomDto.builder() in your code
public record JitsiRoomDto(
    Long conversationId,
    String roomName,
    String jitsiUrl,
    String jwt
) {}