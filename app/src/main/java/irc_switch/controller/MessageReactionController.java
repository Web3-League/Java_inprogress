package irc_switch.controller;

import irc_switch.model.Message;
import irc_switch.model.MessageReaction;
import irc_switch.model.User;
import irc_switch.service.MessageReactionService;
import irc_switch.service.MessageService;
import irc_switch.service.UserService;
import irc_switch.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/reactions")
public class MessageReactionController {

    @Autowired
    private MessageReactionService messageReactionService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @PostMapping("/{userId}/{messageId}")
    public ResponseEntity<?> addReaction(@PathVariable Long userId, @PathVariable Long messageId, @RequestBody String reaction) {
        User user = userService.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Message message = messageService.findById(messageId).orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        messageReactionService.addReaction(user, message, reaction);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{messageId}")
    public List<MessageReaction> getReactions(@PathVariable Long messageId) {
        Message message = messageService.findById(messageId).orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        return messageReactionService.getReactions(message);
    }
}
