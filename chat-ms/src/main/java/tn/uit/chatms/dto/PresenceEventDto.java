package tn.uit.chatms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PresenceEventDto {

    private String userId;
    private String status;
    private LocalDateTime lastSeen;
    private LocalDateTime serverTime;
}
