package irc_switch.controller;

import irc_switch.dto.PrivateMessageDto;
import irc_switch.model.User;
import irc_switch.service.PrivateMessageService;
import irc_switch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/private-messages")
@CrossOrigin(origins = "http://localhost:3000")
public class PrivateMessageController {

    @Autowired
    private PrivateMessageService privateMessageService;

    @Autowired
    private UserService userService;

    @PostMapping("/{recipientId}")
    public PrivateMessageDto sendPrivateMessage(@PathVariable Long recipientId, @RequestBody PrivateMessageDto privateMessageDto, Principal principal) {
        User sender = userService.findByUsername(principal.getName()).orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        privateMessageDto.setSenderId(sender.getId());
        return privateMessageService.sendMessage(sender.getId(), recipientId, privateMessageDto.getContent());
    }

    @GetMapping("/{userId}/{recipientId}")
    public List<PrivateMessageDto> getMessagesBetweenUsers(@PathVariable Long userId, @PathVariable Long recipientId) {
        return privateMessageService.getMessagesBetweenUsers(userId, recipientId);
    }

    @GetMapping("/conversations/{userId}")
    public List<User> getPrivateMessageConversations(@PathVariable Long userId) {
        return privateMessageService.getPrivateMessageConversations(userId);
    }
}
