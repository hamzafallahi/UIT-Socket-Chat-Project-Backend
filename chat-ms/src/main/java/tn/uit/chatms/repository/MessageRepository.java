package tn.uit.chatms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.uit.chatms.entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversation_IdOrderByTimestampAsc(Long conversationId);
}
