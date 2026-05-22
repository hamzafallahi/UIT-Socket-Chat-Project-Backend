package tn.uit.chatms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private Long conversationId;
    private String senderId;
    private String content;
}
