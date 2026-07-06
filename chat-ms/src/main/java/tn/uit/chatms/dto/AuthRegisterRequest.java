package tn.uit.chatms.dto;
import lombok.Builder;

@Builder
public record AuthRegisterRequest(
    String username,
    String password,
    String displayName
) {}