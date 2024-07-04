package irc_switch.service;

import irc_switch.model.Message;
import irc_switch.model.User;
import irc_switch.model.MessageReadStatus;
import irc_switch.repository.MessageReadStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List; // Add this import statement

@Service
public class MessageReadStatusService {

    @Autowired
    private MessageReadStatusRepository messageReadStatusRepository;

    public void markAsRead(User user, Message message) {
        MessageReadStatus status = new MessageReadStatus();
        status.setUser(user);
        status.setMessage(message);
        status.setRead(true);
        messageReadStatusRepository.save(status);
    }

    public List<MessageReadStatus> getReadStatusesByMessage(Message message) {
        return messageReadStatusRepository.findByMessage(message);
    }
}
