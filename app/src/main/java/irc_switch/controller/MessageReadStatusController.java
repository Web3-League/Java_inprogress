package irc_switch.controller;

import irc_switch.model.Message;
import irc_switch.model.User;
import irc_switch.service.MessageReadStatusService;
import irc_switch.service.MessageService;
import irc_switch.service.UserService;
import irc_switch.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/messages")
public class MessageReadStatusController {

    @Autowired
    private MessageReadStatusService messageReadStatusService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/{messageId}/read/{userId}")
    public ResponseEntity<Map<String, String>> markAsRead(@PathVariable Long messageId, @PathVariable Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Message message = messageService.findById(messageId).orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        messageReadStatusService.markAsRead(user, message);

        Map<String, String> response = new HashMap<>();
        response.put("status", "Message marked as read");
        return ResponseEntity.ok(response);
    }
}
