package irc_switch.dto;

import java.util.List;
import irc_switch.model.User;

public class InitialConversationDto {
    private User recipient;
    private List<PrivateMessageDto> messages;

    public InitialConversationDto(User recipient, List<PrivateMessageDto> messages) {
        this.recipient = recipient;
        this.messages = messages;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public List<PrivateMessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<PrivateMessageDto> messages) {
        this.messages = messages;
    }
}
