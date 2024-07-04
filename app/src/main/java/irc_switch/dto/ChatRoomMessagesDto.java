package irc_switch.dto;

import java.util.List;

public class ChatRoomMessagesDto {
    private Long chatRoomId;
    private List<MessageDto> messages;

    public ChatRoomMessagesDto(Long chatRoomId, List<MessageDto> messages) {
        this.chatRoomId = chatRoomId;
        this.messages = messages;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public List<MessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDto> messages) {
        this.messages = messages;
    }
}
