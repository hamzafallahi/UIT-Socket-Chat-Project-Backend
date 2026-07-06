package tn.uit.chatms.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record PresenceSnapshotDto(
    List<String> onlineUsers,
    Map<String, LocalDateTime> lastSeen,
    LocalDateTime serverTime
) {}