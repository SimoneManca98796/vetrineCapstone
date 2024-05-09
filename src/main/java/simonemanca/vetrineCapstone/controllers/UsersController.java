package simonemanca.vetrineCapstone.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import simonemanca.vetrineCapstone.entities.User;
import simonemanca.vetrineCapstone.services.FileStorageService;
import simonemanca.vetrineCapstone.services.UserService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

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
            String fileName = fileStorageService.storeFile(file); // Questo metodo dovrebbe gestire l'effettivo salvataggio del file

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/path/to/file/")
                    .path(fileName)
                    .toUriString();

            currentUser.setAvatarURL(fileDownloadUri);
            userService.update(currentUser);

            return ResponseEntity.ok().body(Map.of("fileName", fileName, "uri", fileDownloadUri, "type", file.getContentType(), "size", file.getSize()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload the file: " + ex.getMessage());
        }
    }
}
