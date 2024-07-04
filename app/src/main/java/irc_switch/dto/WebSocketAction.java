package irc_switch.dto;

public class WebSocketAction {
    private String action;
    private Long chatRoomId;

    public WebSocketAction() {
    }

    public WebSocketAction(String action, Long chatRoomId) {
        this.action = action;
        this.chatRoomId = chatRoomId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }
}
