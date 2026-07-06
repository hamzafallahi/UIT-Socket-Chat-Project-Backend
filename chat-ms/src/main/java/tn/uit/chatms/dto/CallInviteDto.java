package tn.uit.chatms.dto;

import lombok.Builder;

@Builder // Keep this if your services use CallInviteDto.builder()
public record CallInviteDto(
    Long conversationId,
    String fromUserId,
    String fromDisplayName,
    String toUserId,
    String roomName,
    String jitsiUrl,
    String jwt
) {}