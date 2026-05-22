package tn.uit.chatms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallInviteDto {

    private Long conversationId;
    private String fromUserId;
    private String fromDisplayName;
    private String toUserId;
    private String roomName;
    private String jitsiUrl;
    private String jwt;
}
