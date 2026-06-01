package tn.uit.chatms.service;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import tn.uit.chatms.dto.PresenceEventDto;
import tn.uit.chatms.dto.PresenceSnapshotDto;
import tn.uit.chatms.entity.User;
import tn.uit.chatms.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PresenceEventListener {

    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private final ConcurrentHashMap<String, AtomicInteger> sessionCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> sessionUsers = new ConcurrentHashMap<>();

    public PresenceEventListener(UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleSessionConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = resolveUserId(accessor);
        String sessionId = accessor.getSessionId();
        if (userId == null || sessionId == null) {
            return;
        }

        sessionUsers.put(sessionId, userId);
        sessionCounts.computeIfAbsent(userId, key -> new AtomicInteger(0)).incrementAndGet();

        LocalDateTime now = LocalDateTime.now();
        messagingTemplate.convertAndSend("/topic/presence", new PresenceEventDto(userId, "ONLINE", null, now));
        messagingTemplate.convertAndSend("/topic/presence/" + userId, buildSnapshot(now));
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        if (sessionId == null) {
            return;
        }

        String userId = sessionUsers.remove(sessionId);
        if (userId == null) {
            return;
        }

        AtomicInteger counter = sessionCounts.get(userId);
        int remaining = counter != null ? counter.decrementAndGet() : 0;
        if (remaining <= 0) {
            sessionCounts.remove(userId);
            LocalDateTime now = LocalDateTime.now();
            userRepository.findByUsername(userId).ifPresent(user -> {
                user.setLastSeen(now);
                userRepository.save(user);
            });
            messagingTemplate.convertAndSend("/topic/presence", new PresenceEventDto(userId, "OFFLINE", now, now));
        }
    }

    private PresenceSnapshotDto buildSnapshot(LocalDateTime now) {
        List<String> onlineUsers = sessionCounts.entrySet().stream()
                .filter(entry -> entry.getValue().get() > 0)
                .map(Map.Entry::getKey)
                .sorted()
                .toList();

        Map<String, LocalDateTime> lastSeen = new HashMap<>();
        for (User user : userRepository.findAll()) {
            if (user.getUsername() != null) {
                lastSeen.put(user.getUsername(), user.getLastSeen());
            }
        }

        return new PresenceSnapshotDto(onlineUsers, lastSeen, now);
    }

    private String resolveUserId(StompHeaderAccessor accessor) {
        Principal principal = accessor.getUser();
        if (principal != null && principal.getName() != null && !principal.getName().isBlank()) {
            return principal.getName();
        }

        String userId = accessor.getFirstNativeHeader("userId");
        if (userId == null || userId.isBlank()) {
            userId = accessor.getFirstNativeHeader("login");
        }

        if (userId == null || userId.isBlank()) {
            return null;
        }

        return userId;
    }
}
