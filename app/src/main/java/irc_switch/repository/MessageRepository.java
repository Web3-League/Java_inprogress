package irc_switch.repository;

import irc_switch.model.ChatRoom;
import irc_switch.model.Message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatRoomId(Long chatRoomId);
    List<Message> findByChatRoom(ChatRoom chatRoom);
    List<Message> findAllById(Long chatRoomId);
    List<Message> findByUserId(Long userId);
    List<Message> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);
}

