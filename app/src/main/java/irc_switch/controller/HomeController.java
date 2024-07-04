package irc_switch.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class HomeController {

    @GetMapping("/profile")
    public Map<String, Object> profile(@AuthenticationPrincipal OidcUser principal) {
        return Map.of(
            "name", principal.getFullName(),
            "email", principal.getEmail()
        );
    }
}

