package irc_switch.dto;

public class TypingStatusDto {
    private Long userId;
    private Long chatRoomId;
    private boolean isTyping;

    public TypingStatusDto(Long userId, Long chatRoomId, boolean isTyping) {
        this.userId = userId;
        this.chatRoomId = chatRoomId;
        this.isTyping = isTyping;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
    }
}
