package tn.uit.chatms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.uit.chatms.entity.Conversation;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> findByUsersUsername(String userId);
}
