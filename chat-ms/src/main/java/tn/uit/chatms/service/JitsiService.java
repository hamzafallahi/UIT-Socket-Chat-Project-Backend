package tn.uit.chatms.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import tn.uit.chatms.dto.JitsiRoomDto;
import tn.uit.chatms.entity.Conversation;
import tn.uit.chatms.entity.JitsiRoom;
import tn.uit.chatms.repository.ConversationRepository;
import tn.uit.chatms.repository.JitsiRoomRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
public class JitsiService {

    private final JitsiRoomRepository jitsiRoomRepository;
        private final ConversationRepository conversationRepository;

    @Value("${jitsi.server.url:https://meet.jit.si}")
    private String jitsiServerUrl;

    @Value("${jitsi.app.id:uit-project}")
    private String jitsiAppId;

    @Value("${jitsi.app.secret:your-jitsi-secret-key-min-256-bits-long-here}")
    private String jitsiAppSecret;

    @Value("${jitsi.jwt.enabled:false}")
    private boolean jwtEnabled;

        public JitsiService(JitsiRoomRepository jitsiRoomRepository, ConversationRepository conversationRepository) {
        this.jitsiRoomRepository = jitsiRoomRepository;
                this.conversationRepository = conversationRepository;
    }

    /**
     * Create or retrieve an active Jitsi room for a conversation.
     */
    public JitsiRoomDto getOrCreateRoom(Long conversationId, String userId, String displayName) {
        JitsiRoom room = jitsiRoomRepository.findByConversation_IdAndActiveTrue(conversationId)
                                .orElseGet(() -> createRoom(conversationId, userId));

        String jwt = jwtEnabled ? generateJitsiJwt(room.getRoomName(), userId, displayName) : null;

        return JitsiRoomDto.builder()
                .conversationId(conversationId)
                .roomName(room.getRoomName())
                .jitsiUrl(jitsiServerUrl + "/" + room.getRoomName())
                .jwt(jwt)
                .build();
    }

    /**
     * End an active Jitsi room.
     */
    public void endRoom(Long conversationId) {
        jitsiRoomRepository.findByConversation_IdAndActiveTrue(conversationId)
                .ifPresent(room -> {
                    room.setActive(false);
                    jitsiRoomRepository.save(room);
                });
    }

    private JitsiRoom createRoom(Long conversationId, String userId) {
                // Keep one stable room per conversation when possible to reduce repeated prompts.
                String stableRoomName = "uit-conv-" + conversationId;
                JitsiRoom existing = jitsiRoomRepository.findByRoomName(stableRoomName).orElse(null);
                if (existing != null) {
                        existing.setActive(true);
                        return jitsiRoomRepository.save(existing);
                }

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found: " + conversationId));

        JitsiRoom room = JitsiRoom.builder()
                                .roomName(stableRoomName)
                .conversation(conversation)
                .createdBy(userId)
                .build();
        return jitsiRoomRepository.save(room);
    }

    /**
     * Generate a JWT token for Jitsi Meet authentication.
     * Only used when jitsi.jwt.enabled=true (self-hosted Jitsi with JWT auth).
     */
    private String generateJitsiJwt(String roomName, String userId, String displayName) {
        SecretKey key = Keys.hmacShaKeyFor(jitsiAppSecret.getBytes(StandardCharsets.UTF_8));
        Instant now = Instant.now();

        return Jwts.builder()
                .header().add("typ", "JWT").and()
                .issuer(jitsiAppId)
                .subject(jitsiServerUrl)
                .claim("room", roomName)
                .claim("context", Map.of(
                        "user", Map.of(
                                "id", userId,
                                "name", displayName
                        )
                ))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(4, ChronoUnit.HOURS)))
                .signWith(key)
                .compact();
    }
}
