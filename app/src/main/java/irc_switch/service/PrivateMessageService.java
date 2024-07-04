package irc_switch.service;

import irc_switch.dto.PrivateMessageDto;
import irc_switch.model.PrivateMessage;
import irc_switch.model.User;
import irc_switch.repository.PrivateMessageRepository;
import irc_switch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrivateMessageService {

    @Autowired
    private PrivateMessageRepository privateMessageRepository;

    @Autowired
    private UserRepository userRepository;

    public PrivateMessageDto sendMessage(Long senderId, Long recipientId, String content) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new IllegalArgumentException("Recipient not found"));

        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setSender(sender);
        privateMessage.setRecipient(recipient);
        privateMessage.setContent(content);

        PrivateMessage savedMessage = privateMessageRepository.save(privateMessage);
        return new PrivateMessageDto(
            savedMessage.getSender().getId(),
            savedMessage.getRecipient().getId(),
            savedMessage.getContent(),
            savedMessage.getSender().getPseudonym(),
            savedMessage.getSender().getUsername(),
            savedMessage.getCreatedAt()
        );
    }

    public List<PrivateMessageDto> getMessagesBetweenUsers(Long userId, Long recipientId) {
        User sender = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new IllegalArgumentException("Recipient not found"));

        List<PrivateMessage> messages = privateMessageRepository.findBySenderAndRecipientOrRecipientAndSenderOrderByCreatedAtAsc(sender, recipient, recipient, sender);
        return messages.stream().map(message -> new PrivateMessageDto(
                message.getSender().getId(),
                message.getRecipient().getId(),
                message.getContent(),
                message.getSender().getPseudonym() != null ? message.getSender().getPseudonym() : message.getSender().getUsername(),
                message.getRecipient().getPseudonym() != null ? message.getRecipient().getPseudonym() : message.getRecipient().getUsername(),
                message.getCreatedAt()
        )).collect(Collectors.toList());
    }

    public List<User> getPrivateMessageConversations(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<PrivateMessage> messages = privateMessageRepository.findDistinctBySenderOrRecipient(user, user);
        return messages.stream()
                .map(message -> message.getSender().getId().equals(userId) ? message.getRecipient() : message.getSender())
                .distinct()
                .collect(Collectors.toList());
    }
}
