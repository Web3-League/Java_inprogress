package irc_switch.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient; // New field for private messaging

    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;



    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return user != null ? user.getUsername() : null;
    }

    public void setUsername(String username) {
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setUsername(username);
    }

    public String getPseudonym() {
        return user != null ? user.getPseudonym() : null;
    }

    public void setPseudonym(String pseudonym) {
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setPseudonym(pseudonym);
    }

    public void setMessage(String message) {
        this.setContent(message);
    }

    public String getMessage() {
        return this.getContent();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
}
