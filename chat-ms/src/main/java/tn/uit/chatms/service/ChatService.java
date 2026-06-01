package tn.uit.chatms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.uit.chatms.dto.ChatMessageDto;
import tn.uit.chatms.dto.CreateConversationDto;
import tn.uit.chatms.entity.*;
import tn.uit.chatms.repository.ConversationRepository;
import tn.uit.chatms.repository.MessageRepository;
import tn.uit.chatms.repository.UserRepository;

import java.util.List;

@Service
public class ChatService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    public ChatService(
            MessageRepository messageRepository,
            ConversationRepository conversationRepository,
            UserRepository userRepository
    ) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
    }

    public Message saveMessage(ChatMessageDto dto) {
        Conversation conv = conversationRepository.findById(dto.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        User sender = userRepository.findByUsername(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getSenderId()));
        Message msg = Message.builder()
                .conversation(conv)
                .sender(sender)
                .content(dto.getContent())
                .fileUrl(dto.getFileUrl()) 
                .messageType(dto.getMessageType())
                .build();
        return messageRepository.save(msg);
    }

    public List<Message> getMessages(Long conversationId) {
        return messageRepository.findByConversation_IdOrderByTimestampAsc(conversationId);
    }

    @Transactional
    public Conversation createConversation(CreateConversationDto dto) {
        Conversation conv = Conversation.builder()
                .type(dto.getType() != null ? ConversationType.valueOf(dto.getType()) : ConversationType.PRIVATE)
                .build();
        conv = conversationRepository.save(conv);

        List<User> users = userRepository.findByUsernameIn(dto.getParticipants());
        for (User user : users) {
            user.getConversations().add(conv);
        }
        userRepository.saveAll(users);

        conv.setUsers(users);
        return conv;
    }

    public List<Conversation> getConversations(String userId) {
        return conversationRepository.findByUsersUsername(userId);
    }

    public Conversation getConversation(Long id) {
        return conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found: " + id));
    }
}
