package irc_switch.repository;

import irc_switch.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional; // Add this import statement

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByName(String name);
}
