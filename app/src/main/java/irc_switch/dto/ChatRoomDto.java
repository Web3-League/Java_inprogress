
package irc_switch.dto;

public class ChatRoomDto {
    private Long chatRoomId;
    private String name;


    // Getters and Setters

    public Long getchatRoomId() {
        return chatRoomId;
    }

    public void setchatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
