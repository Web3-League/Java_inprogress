package irc_switch.repository;

import irc_switch.model.PrivateMessage;
import irc_switch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {
    List<PrivateMessage> findBySenderAndRecipientOrRecipientAndSenderOrderByCreatedAtAsc(User sender, User recipient, User recipient2, User sender2);
    List<PrivateMessage> findDistinctBySenderOrRecipient(User sender, User recipient);
}
