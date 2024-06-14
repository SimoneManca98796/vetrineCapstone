package simonemanca.vetrineCapstone.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simonemanca.vetrineCapstone.entities.Notifica;
import simonemanca.vetrineCapstone.services.NotificaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class NotificaController {

    private static final Logger logger = LoggerFactory.getLogger(NotificaController.class);

    @Autowired
    private NotificaService notificaService;

    @GetMapping("/notifications")
    public ResponseEntity<List<Notifica>> getAllNotifiche() {
        try {
            List<Notifica> notifiche = notificaService.getAllNotifiche();
            List<Notifica> notificheDaCloudinary = notifiche.stream()
                    .filter(notifica -> notifica.getAvatarURL() != null && notifica.getAvatarURL().contains("cloudinary"))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(notificheDaCloudinary);
        } catch (Exception e) {
            logger.error("Error fetching notifications", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/notifications")
    public ResponseEntity<Notifica> createNotifica(@RequestBody Notifica notifica) {
        try {
            Notifica createdNotifica = notificaService.createNotifica(notifica);
            return ResponseEntity.ok(createdNotifica);
        } catch (Exception e) {
            logger.error("Error creating notification", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/notifications/news")
    public ResponseEntity<Void> createNewsNotification(
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam String url) {
        try {
            notificaService.createNewsNotification(title, message, url);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error("Error creating news notification", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/notifications/fetch-news")
    public ResponseEntity<Void> fetchNews() {
        try {
            // newsService.fetchAndSaveNews();
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error("Error fetching news notifications", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/notifications/{id}")
    public ResponseEntity<Notifica> getNotificaById(@PathVariable int id) {
        try {
            Notifica notifica = notificaService.getNotificaById(id);
            if (notifica != null) {
                return ResponseEntity.ok(notifica);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error fetching notification with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/notifications/{id}")
    public ResponseEntity<Void> deleteNotifica(@PathVariable int id) {
        try {
            notificaService.deleteNotifica(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting notification with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/notifications/unread")
    public ResponseEntity<List<Notifica>> getUnreadNotifications() {
        try {
            List<Notifica> unreadNotifications = notificaService.getUnreadNotifiche();
            List<Notifica> unreadNotificationsDaCloudinary = unreadNotifications.stream()
                    .filter(notifica -> notifica.getAvatarURL() != null && notifica.getAvatarURL().contains("cloudinary"))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(unreadNotificationsDaCloudinary);
        } catch (Exception e) {
            logger.error("Error fetching unread notifications", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}













