package irc_switch.controller;

import irc_switch.dto.ChatRoomDto;
import irc_switch.dto.SetPseudonymDto;
import irc_switch.model.ChatRoom;
import irc_switch.model.Message;
import irc_switch.model.User;
import irc_switch.service.ChatRoomService;
import irc_switch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChatRoomService chatRoomService;

    @PostMapping("/pseudonym")
    public Map<String, Object> setPseudonym(@RequestBody SetPseudonymDto pseudonymDto) {
        Map<String, Object> response = new HashMap<>();
        String username = getCurrentUsername();
        
        if (username == null) {
            response.put("success", false);
            response.put("message", "User not authenticated");
            return response;
        }

        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPseudonym(pseudonymDto.getPseudonym());
            userService.save(user, null);
            response.put("success", true);
            response.put("message", "Pseudonym set successfully");
        } else {
            response.put("success", false);
            response.put("message", "User not found");
        }
        return response;
    }

    @PostMapping("/create-room")
    public Map<String, Object> createChatRoom(@RequestBody ChatRoomDto chatRoomDto) {
        Map<String, Object> response = new HashMap<>();
        String username = getCurrentUsername();
    
        if (username == null) {
            response.put("success", false);
            response.put("message", "User not authenticated");
            return response;
        }
    
        Optional<User> userOptional = userService.findByUsername(username);
    
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            ChatRoom chatRoom = chatRoomService.createChatRoom(chatRoomDto.getName(), user);
            response.put("success", true);
            response.put("message", "Chat room created successfully");
            response.put("chatRoomId", chatRoom.getId()); // Ensure chatRoomId is added to response
        } else {
            response.put("success", false);
            response.put("message", "User not found");
        }
        return response;
    }
    
    @GetMapping("/rooms")
    public List<ChatRoom> listChatRooms() {
        return chatRoomService.findAll();
    }

    @GetMapping("/rooms/{roomId}")
    public Optional<ChatRoom> getChatRoom(@PathVariable Long roomId) {
        return chatRoomService.findById(roomId);
    }

    @PostMapping("/rooms/{roomId}/messages")
    public Map<String, Object> sendMessage(@PathVariable Long roomId, @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String content = request.get("content");
        String username = getCurrentUsername();
        Optional<User> userOptional = userService.findByUsername(username);
        Optional<ChatRoom> chatRoomOptional = chatRoomService.findById(roomId);

        if (userOptional.isPresent() && chatRoomOptional.isPresent()) {
            User user = userOptional.get();
            ChatRoom chatRoom = chatRoomOptional.get();
            chatRoomService.saveMessage(chatRoom, user, content);
            response.put("success", true);
            response.put("message", "Message sent successfully");
        } else {
            response.put("success", false);
            response.put("message", "User or chat room not found");
        }
        return response;
    }
    
    @GetMapping("/rooms/{roomId}/messages")
    public List<Message> getMessages(@PathVariable Long roomId) {
        Optional<ChatRoom> chatRoomOptional = chatRoomService.findById(roomId);
        return chatRoomOptional.map(chatRoomService::getMessagesForChatRoom).orElse(null);
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
    }
}
