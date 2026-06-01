package tn.uit.chatms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PresenceSnapshotDto {

    private List<String> onlineUsers;
    private Map<String, LocalDateTime> lastSeen;
    private LocalDateTime serverTime;
}
