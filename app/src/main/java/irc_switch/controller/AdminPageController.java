package irc_switch.controller;

import irc_switch.config.AdminUserConfig;
import irc_switch.model.User;
import irc_switch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:8080")
public class AdminPageController {

    @Autowired
    private AdminUserConfig adminUserConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private boolean adminPageVisible = true;

    @PostConstruct
    public void init() {
        createAdminUser();
    }

    private void createAdminUser() {
        String adminUsername = "admin";
        if (userService.findByUsername(adminUsername).isEmpty()) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(bCryptPasswordEncoder.encode(adminUserConfig.getAdminPassword()));
            admin.setEmail("admin@example.com");

            Set<String> roles = new HashSet<>();
            roles.add("ROLE_ADMIN");

            userService.save(admin, roles);
        }
    }

    @GetMapping("/admin-credentials")
    public String showAdminPage(Model model) {
        if (!adminPageVisible) {
            return "redirect:/"; // Redirect to home if the page is deleted
        }
        String adminPassword = adminUserConfig.getAdminPassword();
        model.addAttribute("adminPassword", adminPassword);
        return "admin-credentials"; // Ensure you have an admin-credentials.html in your templates folder
    }

    @PostMapping("/admin-credentials/delete")
    public String deleteAdminPage() {
        adminPageVisible = false;
        return "redirect:/"; // Redirect to home after deletion
    }
}
