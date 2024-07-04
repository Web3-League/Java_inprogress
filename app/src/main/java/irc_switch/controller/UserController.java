package irc_switch.controller;

import irc_switch.dto.UserRegistrationDto;
import irc_switch.dto.UserDto;
import irc_switch.dto.UserLoginDto;
import irc_switch.model.User;
import irc_switch.service.UserService;
import irc_switch.service.MyUserDetailsService;
import irc_switch.util.JwtUtil;
import irc_switch.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody UserRegistrationDto userDto) {
        Map<String, Object> response = new HashMap<>();
        String username = userDto.getUsername();
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        Optional<User> userByUsername = userService.findByUsername(username);
        if (userByUsername.isPresent()) {
            response.put("success", false);
            response.put("message", "Username already exists");
            return response;
        }

        // No need to check for email uniqueness as per the requirements

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER"); // Default role

        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setEmail(email);

        userService.save(user, roles);
        response.put("success", true);
        response.put("message", "Registration successful");
        return response;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody UserLoginDto userDto) {
        Map<String, Object> response = new HashMap<>();
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        Optional<User> existingUser = userService.findByUsername(username);
        if (existingUser.isPresent() && bCryptPasswordEncoder.matches(password, existingUser.get().getPassword())) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String token = jwtUtil.generateToken(userDetails);

            response.put("success", true);
            response.put("message", "Login successful");
            response.put("token", token);
            response.put("userId", existingUser.get().getId());
        } else {
            response.put("success", false);
            response.put("message", "Invalid username or password");
        }
        return response;
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/online/{chatRoomId}")
    public List<UserDto> getUsersByChatRoomId(@PathVariable Long chatRoomId) {
        List<User> users = userService.getUsersByChatRoomId(chatRoomId);
        return users.stream()
                .map(user -> new UserDto(user.getId(), user.getUsername(), user.getPseudonym()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}/pseudonym")
    public ResponseEntity<Map<String, String>> getPseudonym(@PathVariable Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Map<String, String> response = new HashMap<>();
        response.put("pseudonym", user.getPseudonym());
        return ResponseEntity.ok(response);
    }

}

