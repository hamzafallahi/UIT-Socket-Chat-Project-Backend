package tn.uit.chatms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.uit.chatms.entity.JitsiRoom;

import java.util.Optional;

public interface JitsiRoomRepository extends JpaRepository<JitsiRoom, Long> {

    Optional<JitsiRoom> findByConversation_IdAndActiveTrue(Long conversationId);

    Optional<JitsiRoom> findByRoomName(String roomName);
}
