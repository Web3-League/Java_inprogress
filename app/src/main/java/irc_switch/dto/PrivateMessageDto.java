package irc_switch.dto;

import java.time.LocalDateTime;

public class PrivateMessageDto {
    private Long senderId;
    private Long recipientId;
    private String content;
    private String senderPseudonym;
    private String senderUsername;
    private LocalDateTime createdAt;

    // Constructors, getters, and setters
    public PrivateMessageDto() {
    }

    public PrivateMessageDto(Long senderId, Long recipientId, String content, String senderPseudonym, String senderUsername, LocalDateTime createdAt) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
        this.senderPseudonym = senderPseudonym;
        this.senderUsername = senderUsername;
        this.createdAt = createdAt;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderPseudonym() {
        return senderPseudonym;
    }

    public void setSenderPseudonym(String senderPseudonym) {
        this.senderPseudonym = senderPseudonym;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
