package irc_switch.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class AdminUserConfig {

    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public String getAdminPassword() {
        return adminPassword;
    }

    public String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
}

