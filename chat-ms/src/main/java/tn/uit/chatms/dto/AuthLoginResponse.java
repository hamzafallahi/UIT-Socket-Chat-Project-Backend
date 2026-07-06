package tn.uit.chatms.dto;
import lombok.Builder;

@Builder
public record AuthLoginResponse(
    String token,
    String userId,
    String displayName,
    String role
) {}