package irc_switch.service;

import irc_switch.model.Message;
import irc_switch.model.MessageReaction;
import irc_switch.model.User;
import irc_switch.repository.MessageReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageReactionService {

    @Autowired
    private MessageReactionRepository messageReactionRepository;

    public void addReaction(User user, Message message, String reaction) {
        MessageReaction messageReaction = new MessageReaction();
        messageReaction.setUser(user);
        messageReaction.setMessage(message);
        messageReaction.setReaction(reaction);
        messageReactionRepository.save(messageReaction);
    }

    public List<MessageReaction> getReactions(Message message) {
        return messageReactionRepository.findByMessage(message);
    }
}
