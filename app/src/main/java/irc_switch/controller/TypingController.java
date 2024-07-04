package irc_switch.controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class TypingController {

    @MessageMapping("/typing")
    @SendTo("/topic/typing")
    public TypingStatus typing(TypingStatus status) {
        return status;
    }

    public static class TypingStatus {
        private String username;
        private boolean isTyping;

        // getters and setters

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public boolean isTyping() {
            return isTyping;
        }

        public void setTyping(boolean typing) {
            isTyping = typing;
        }
    }
}
