package tn.uit.chatms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.uit.chatms.dto.JitsiRoomDto;
import tn.uit.chatms.service.JitsiService;

@RestController
@RequestMapping("/api/jitsi")

public class JitsiController {

    private final JitsiService jitsiService;

    public JitsiController(JitsiService jitsiService) {
        this.jitsiService = jitsiService;
    }


    @PostMapping("/room/{conversationId}")
    public ResponseEntity<JitsiRoomDto> startVideoCall(
            @PathVariable Long conversationId,
            @RequestParam String userId,
            @RequestParam(defaultValue = "User") String displayName) {
        return ResponseEntity.ok(jitsiService.getOrCreateRoom(conversationId, userId, displayName));
    }

    @DeleteMapping("/room/{conversationId}")
    public ResponseEntity<Void> endVideoCall(@PathVariable Long conversationId) {
        jitsiService.endRoom(conversationId);
        return ResponseEntity.noContent().build();
    }
}
