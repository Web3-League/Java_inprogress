package irc_switch.repository;

import irc_switch.model.MessageReadStatus;
import irc_switch.model.User;
import irc_switch.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageReadStatusRepository extends JpaRepository<MessageReadStatus, Long> {
    List<MessageReadStatus> findByUserAndMessage(User user, Message message);
    List<MessageReadStatus> findByMessage(Message message);
}
