package simonemanca.vetrineCapstone.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import simonemanca.vetrineCapstone.entities.User;
import simonemanca.vetrineCapstone.exceptions.BadRequestException;
import simonemanca.vetrineCapstone.exceptions.NotFoundException;
import simonemanca.vetrineCapstone.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<User> getUsers(int page, int size, String sortBy) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRepository.findAll(pageable);
    }

    public User save(User user) {
        Optional<User> existingUserOpt = userRepository.findByEmail(user.getEmail());
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            if (!existingUser.getId().equals(user.getId())) {
                throw new BadRequestException("L'email " + user.getEmail() + " è già in uso!");
            }
        }
        return userRepository.save(user);
    }

    public User findById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Utente non trovato con ID: " + userId));
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public void delete(UUID userId) {
        userRepository.deleteById(userId);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovato!"));
    }

    public User findByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken).orElseThrow(() -> new NotFoundException("Token di reset non valido!"));
    }

    public String generatePasswordResetToken(User user) {
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiration(LocalDateTime.now().plusHours(1)); // Il token scade dopo 1 ora
        userRepository.save(user);
        return token;
    }
}


