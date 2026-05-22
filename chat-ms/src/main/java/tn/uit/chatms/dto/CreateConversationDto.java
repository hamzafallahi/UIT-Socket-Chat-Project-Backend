package tn.uit.chatms.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateConversationDto {

    private List<String> participants;
    private String type; // PRIVATE or SUPPORT
}
