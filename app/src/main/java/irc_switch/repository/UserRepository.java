package irc_switch.repository;

import irc_switch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.chatRooms c WHERE c.id = :chatRoomId")
    List<User> findUsersByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
