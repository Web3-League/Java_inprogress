package irc_switch.dto;

public class UserDto {
    private Long id;
    private String username;
    private String pseudonym;

    public UserDto(Long id, String username, String pseudonym) {
        this.id = id;
        this.username = username;
        this.pseudonym = pseudonym;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }
}
