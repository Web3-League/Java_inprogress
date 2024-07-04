package irc_switch.service;

import irc_switch.model.User;
import irc_switch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void save(User user, Set<String> roles) {
        user.setRoles(roles);
        userRepository.save(user);
    }

    public void updatePseudonym(String username, String pseudonym) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPseudonym(pseudonym);
            userRepository.save(user);
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> getUsersByChatRoomId(Long chatRoomId) {
        return userRepository.findUsersByChatRoomId(chatRoomId);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }
}
