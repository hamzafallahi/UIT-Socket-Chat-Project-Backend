package tn.uit.chatms.dto;

import java.time.LocalDateTime;

public record PresenceEventDto(
    String userId,
    String status,
    LocalDateTime lastSeen,
    LocalDateTime serverTime
) {}