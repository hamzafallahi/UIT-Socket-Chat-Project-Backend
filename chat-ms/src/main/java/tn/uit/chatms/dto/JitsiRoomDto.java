package tn.uit.chatms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JitsiRoomDto {

    private Long conversationId;
    private String roomName;
    private String jitsiUrl;
    private String jwt;
}
