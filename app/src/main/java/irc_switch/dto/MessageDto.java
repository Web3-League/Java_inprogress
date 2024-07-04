package irc_switch.dto;

public class MessageDto {
    private Long userId;
    private String content;
    private Long chatRoomId;
    private String pseudonym;
    private String username;

    // Default constructor
    public MessageDto() {
    }

    // Constructor with parameters
    public MessageDto(Long userId, String content, Long chatRoomId, String pseudonym, String username) {
        this.userId = userId;
        this.content = content;
        this.chatRoomId = chatRoomId;
        this.pseudonym = pseudonym;
        this.username = username;
    }

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
}

