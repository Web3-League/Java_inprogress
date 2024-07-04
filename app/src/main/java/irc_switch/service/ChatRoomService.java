package irc_switch.service;

import irc_switch.model.ChatRoom;
import irc_switch.model.Message;
import irc_switch.model.User;
import irc_switch.repository.ChatRoomRepository;
import irc_switch.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MessageRepository messageRepository;

    public ChatRoom createChatRoom(String name, User user) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(name);
        chatRoom.getUsers().add(user);
        return chatRoomRepository.save(chatRoom);
    }

    public Optional<ChatRoom> findByName(String name) {
        return chatRoomRepository.findByName(name);
    }

    public Optional<ChatRoom> findById(Long id) {
        return chatRoomRepository.findById(id);
    }

    public ChatRoom addUserToChatRoom(ChatRoom chatRoom, User user) {
        chatRoom.getUsers().add(user);
        return chatRoomRepository.save(chatRoom);
    }

    public Set<User> getUsersInChatRoom(ChatRoom chatRoom) {
        return chatRoom.getUsers();
    }

    public List<ChatRoom> findAll() {
        return chatRoomRepository.findAll();
    }

    public Message saveMessage(ChatRoom chatRoom, User user, String content) {
        Message message = new Message();
        message.setChatRoom(chatRoom);
        message.setUser(user);
        message.setContent(content);
        return messageRepository.save(message);
    }

    public List<Message> getMessagesForChatRoom(ChatRoom chatRoom) {
        return messageRepository.findByChatRoom(chatRoom);
    }
}
