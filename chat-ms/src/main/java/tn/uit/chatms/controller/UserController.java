package tn.uit.chatms.controller;

import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;


import tn.uit.chatms.repository.UserRepository;

@RestController
@RequestMapping("/api/chat/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
        @GetMapping("/search")
    public ResponseEntity<List<Map<String, String>>> searchUsers(@RequestParam String q) {
        List<Map<String, String>> results = userRepository.findByUsernameContainingIgnoreCase(q)
                .stream()
                .map(u -> Map.of(
                        "username", u.getUsername(),
                        "displayName", u.getDisplayName() != null ? u.getDisplayName() : u.getUsername()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }
}
