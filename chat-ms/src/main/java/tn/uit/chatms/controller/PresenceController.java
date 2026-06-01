package tn.uit.chatms.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import tn.uit.chatms.service.PresenceEventListener;

import java.security.Principal;

@Controller
public class PresenceController {

    private final PresenceEventListener presenceEventListener;

    public PresenceController(PresenceEventListener presenceEventListener) {
        this.presenceEventListener = presenceEventListener;
    }

    @MessageMapping("/presence.init")
    public void initPresence(SimpMessageHeaderAccessor accessor) {
        String userId = resolveUserId(accessor);
        if (userId == null) {
            return;
        }
        presenceEventListener.sendSnapshotToUser(userId);
    }

    private String resolveUserId(SimpMessageHeaderAccessor accessor) {
        Principal principal = accessor.getUser();
        if (principal != null && principal.getName() != null && !principal.getName().isBlank()) {
            return principal.getName();
        }

        String userId = accessor.getFirstNativeHeader("userId");
        if (userId == null || userId.isBlank()) {
            userId = accessor.getFirstNativeHeader("login");
        }

        if ((userId == null || userId.isBlank()) && accessor.getSessionId() != null) {
            userId = presenceEventListener.getUserIdForSession(accessor.getSessionId());
        }

        if (userId == null || userId.isBlank()) {
            return null;
        }

        return userId;
    }
}
