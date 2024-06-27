package simonemanca.vetrineCapstone.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import simonemanca.vetrineCapstone.entities.User;
import simonemanca.vetrineCapstone.exceptions.NotFoundException;
import simonemanca.vetrineCapstone.services.CloudinaryService;
import simonemanca.vetrineCapstone.services.EmailService;
import simonemanca.vetrineCapstone.services.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String sortBy) {
        return userService.getUsers(page, size, sortBy);
    }

    @GetMapping("/me")
    public User getProfile(@AuthenticationPrincipal User currentUser) {
        return currentUser;
    }

    @PutMapping("/me")
    public User updateProfile(@AuthenticationPrincipal User currentUser, @RequestBody User updatedUser) {
        updatedUser.setId(currentUser.getId());
        return userService.update(updatedUser);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal User currentUser) {
        userService.delete(currentUser.getId());
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId) {
        return userService.findById(userId);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User updateUserById(@PathVariable UUID userId, @RequestBody User updatedUser) {
        updatedUser.setId(userId);
        return userService.update(updatedUser);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable UUID userId) {
        userService.delete(userId);
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@AuthenticationPrincipal User currentUser, @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                logger.warn("Attempt to upload an empty file.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select a file to upload.");
            }

            logger.info("Uploading file: {}", file.getOriginalFilename());
            Map uploadResult = cloudinaryService.uploadFile(file);

            String fileDownloadUri = uploadResult.get("url").toString();
            currentUser.setAvatarURL(fileDownloadUri);
            userService.update(currentUser);

            logger.info("File uploaded successfully: {}", fileDownloadUri);
            return ResponseEntity.ok().body(Map.of("fileName", file.getOriginalFilename(), "uri", fileDownloadUri, "type", file.getContentType(), "size", file.getSize()));
        } catch (IOException ex) {
            logger.error("Could not upload the file: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload the file: " + ex.getMessage());
        }
    }

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = new FileSystemResource("./uploads/" + filename);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Default MIME type
        try {
            mimeType = file.getFile().toURL().openConnection().getContentType();
        } catch (Exception e) {
            // Handle failure to determine MIME type
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@AuthenticationPrincipal User currentUser, @RequestBody Map<String, String> passwordMap) {
        String newPassword = passwordMap.get("password");
        if (newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password non può essere vuota.");
        }
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userService.update(currentUser);
        return ResponseEntity.ok().body("Password aggiornata con successo.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> emailMap) {
        String email = emailMap.get("email");
        try {
            User user = userService.findByEmail(email);
            String token = userService.generatePasswordResetToken(user); // Genera il token
            emailService.sendPasswordResetEmail(user); // Invia l'email di reset
            return ResponseEntity.ok().body("Le istruzioni per il recupero della password sono state inviate alla tua email.");
        } catch (NotFoundException ex) {
            logger.error("Recupero password fallito: " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Email non trovata: " + ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Errore interno durante il recupero della password", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Errore interno: " + ex.getMessage()));
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> passwordResetMap) {
        String token = passwordResetMap.get("token");
        String newPassword = passwordResetMap.get("newPassword");

        // Stampa di debug
        System.out.println("Token ricevuto: " + token);
        System.out.println("Nuova password ricevuta: " + newPassword);

        try {
            User user = userService.findByResetToken(token);

            if (user == null || user.getTokenExpiration().isBefore(LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token non valido o scaduto.");
            }

            // Stampa di debug
            System.out.println("Utente trovato: " + user.getEmail());

            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            user.setTokenExpiration(null);
            userService.save(user);

            return ResponseEntity.ok("Password aggiornata con successo.");
        } catch (Exception ex) {
            // Stampa di debug per l'eccezione
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il reset della password.");
        }
    }

    @PostMapping("/upload-document")
    public ResponseEntity<?> uploadDocument(@AuthenticationPrincipal User currentUser, @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select a file to upload.");
            }

            Map uploadResult = cloudinaryService.uploadFile(file);

            String documentUrl = uploadResult.get("url").toString();
            currentUser.setDocumentURL(documentUrl);
            userService.update(currentUser);

            return ResponseEntity.ok().body(Map.of("fileName", file.getOriginalFilename(), "url", documentUrl, "type", file.getContentType(), "size", file.getSize()));
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload the file: " + ex.getMessage());
        }
    }
}




