package simonemanca.vetrineCapstone.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import simonemanca.vetrineCapstone.entities.User;
import simonemanca.vetrineCapstone.exceptions.BadRequestException;
import simonemanca.vetrineCapstone.exceptions.UnauthorizedException;
import simonemanca.vetrineCapstone.payloads.NewUserDTO;
import simonemanca.vetrineCapstone.services.AuthService;
import simonemanca.vetrineCapstone.services.UserService;
import simonemanca.vetrineCapstone.services.EmailService;
import simonemanca.vetrineCapstone.entities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody @Validated NewUserDTO newUserDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new BadRequestException("Validation errors", result.getAllErrors());
        }
        User newUser = new User();
        newUser.setName(newUserDTO.name());
        newUser.setSurname(newUserDTO.surname());
        newUser.setEmail(newUserDTO.email());
        newUser.setPassword(passwordEncoder.encode(newUserDTO.password()));
        newUser.setRole(Role.USER);
        User savedUser = userService.save(newUser);

        // Invio email di conferma
        emailService.sendRegistrationEmail(savedUser);

        return savedUser;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginDetails) {
        try {
            User user = authService.authenticate(loginDetails.getEmail(), loginDetails.getPassword());
            String token = authService.generateToken(user);

            // Creazione della mappa con i valori non nulli
            Map<String, Object> responseMap = new HashMap<>();
            if (token != null) {
                responseMap.put("token", token);
            }
            if (user.getAvatarURL() != null) {
                responseMap.put("avatarUrl", user.getAvatarURL());
            }

            return ResponseEntity.ok().body(responseMap);
        } catch (UnauthorizedException ex) {
            logger.error("Login non autorizzato: " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Failed to login: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Errore interno durante il login", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal Server Error: " + ex.getMessage()));
        }
    }
}



