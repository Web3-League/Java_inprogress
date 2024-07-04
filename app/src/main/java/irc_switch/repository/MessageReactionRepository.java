package irc_switch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import irc_switch.model.Message;
import irc_switch.model.MessageReaction;
// MessageReactionRepository.java
public interface MessageReactionRepository extends JpaRepository<MessageReaction, Long> {
    List<MessageReaction> findByMessage(Message message);
}