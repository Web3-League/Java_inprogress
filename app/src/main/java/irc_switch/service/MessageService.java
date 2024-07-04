package irc_switch.service;

import java.time.LocalDateTime;
import irc_switch.dto.MessageDto;
import irc_switch.exception.ResourceNotFoundException;
import irc_switch.model.ChatRoom;
import irc_switch.model.Message;
import irc_switch.model.User;
import irc_switch.repository.MessageRepository;
import irc_switch.repository.ChatRoomRepository;
import irc_switch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Message> getAllMessages(Long chatRoomId) {
        return messageRepository.findByChatRoomId(chatRoomId);
    }

    public List<MessageDto> getMessagesByChatRoomId(Long chatRoomId) {
        List<Message> messages = messageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoomId);
        return messages.stream()
                .map(message -> new MessageDto(
                        message.getUser().getId(),
                        message.getContent(),
                        message.getChatRoom().getId(),
                        message.getUser().getPseudonym(),
                        message.getUser().getUsername()))
                .collect(Collectors.toList());
    }

    public List<MessageDto> getMessagesByUserId(Long userId) {
        List<Message> messages = messageRepository.findByUserId(userId);
        return messages.stream()
                .map(message -> new MessageDto(
                        message.getUser().getId(),
                        message.getContent(),
                        message.getChatRoom() != null ? message.getChatRoom().getId() : null,
                        message.getUser().getPseudonym(),
                        message.getUser().getUsername()))
                .collect(Collectors.toList());
    }

    public Message saveMessage(Long userId, String content, Long chatRoomId, Long recipientId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        ChatRoom chatRoom = null;
        if (chatRoomId != null) {
            chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        }

        User recipient = null;
        if (recipientId != null) {
            recipient = userRepository.findById(recipientId).orElseThrow(() -> new IllegalArgumentException("Recipient not found"));
        }

        Message message = new Message();
        message.setUser(user);
        message.setChatRoom(chatRoom);
        message.setRecipient(recipient);
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message createMessageFromDto(MessageDto messageDto) {
        User user = userRepository.findById(messageDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        ChatRoom chatRoom = null;
        if (messageDto.getChatRoomId() != null) {
            chatRoom = chatRoomRepository.findById(messageDto.getChatRoomId()).orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        }

        Message message = new Message();
        message.setContent(messageDto.getContent());
        message.setUser(user);
        message.setChatRoom(chatRoom);
        message.setCreatedAt(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public void editMessage(Long messageId, String newContent) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        message.setContent(newContent);
        messageRepository.save(message);
    }

    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }

    public Optional<Message> findById(Long id) {
        return messageRepository.findById(id);
    }
}
