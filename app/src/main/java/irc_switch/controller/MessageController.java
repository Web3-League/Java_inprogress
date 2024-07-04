package irc_switch.controller;

import irc_switch.dto.MessageDto;
import irc_switch.model.Message;
import irc_switch.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:3000")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/chatroom/{chatRoomId}")
    public List<MessageDto> getMessagesByChatRoom(@PathVariable Long chatRoomId) {
        List<MessageDto> messages = messageService.getMessagesByChatRoomId(chatRoomId);
        return messages;
    }

    @PostMapping("/send")
    public MessageDto sendMessage(@RequestBody MessageDto messageDto) {
        Message message = messageService.createMessageFromDto(messageDto);
        return new MessageDto(message.getUser().getId(), message.getContent(), message.getChatRoom().getId(), message.getUser().getPseudonym(), message.getUser().getUsername());// Access recipient from Message);
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<?> editMessage(@PathVariable Long messageId, @RequestBody String newContent) {
        messageService.editMessage(messageId, newContent);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }
}


