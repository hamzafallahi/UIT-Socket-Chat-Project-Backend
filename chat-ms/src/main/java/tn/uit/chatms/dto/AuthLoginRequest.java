package tn.uit.chatms.dto;
import lombok.Builder;

@Builder
public record AuthLoginRequest(
    String username,
    String password
) {}