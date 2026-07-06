package tn.uit.chatms.dto;

import java.util.List;

public record CreateConversationDto(
    List<String> participants,
    String type // PRIVATE or SUPPORT
) {}