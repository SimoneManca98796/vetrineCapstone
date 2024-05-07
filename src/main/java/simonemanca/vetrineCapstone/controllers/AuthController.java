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
import simonemanca.vetrineCapstone.entities.Role;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        return userService.save(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginDetails) {
        try {
            User user = authService.authenticate(loginDetails.getEmail(), loginDetails.getPassword());
            String token = authService.generateToken(user);
            // Restituisce un oggetto JSON con il token
            return ResponseEntity.ok().body(Map.of("token", token));
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Failed to login: " + ex.getMessage()));
        }
    }
}


