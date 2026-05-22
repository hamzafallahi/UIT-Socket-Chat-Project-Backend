package tn.uit.chatms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String userId;
    private String displayName;
    private String role;
}
