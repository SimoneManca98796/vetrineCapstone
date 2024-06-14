package simonemanca.vetrineCapstone.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simonemanca.vetrineCapstone.entities.Azienda;
import simonemanca.vetrineCapstone.entities.Notifica;
import simonemanca.vetrineCapstone.entities.Product;
import simonemanca.vetrineCapstone.entities.User;
import simonemanca.vetrineCapstone.repositories.NotificaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@Service
public class NotificaService {

    @Autowired
    private NotificaRepository notificaRepository;

    private static final Logger logger = LoggerFactory.getLogger(NotificaService.class);

    public List<Notifica> getUnreadNotifiche(UUID userId) {
        try {
            logger.info("Fetching unread notifications for user with ID: {}", userId);
            // Filtra le notifiche non lette che non sono state create dall'utente corrente
            return notificaRepository.findByUserIdNotAndReadFalse(userId);
        } catch (Exception e) {
            logger.error("Error fetching unread notifications for user with ID: {}", userId, e);
            throw e;
        }
    }

    public List<Notifica> getAllNotifichePerUtente(UUID userId) {
        try {
            logger.info("Fetching notifications not created by user with ID: {}", userId);
            return notificaRepository.findByUserIdNotAndReadFalse(userId);
        } catch (Exception e) {
            logger.error("Error fetching notifications for user with ID: {}", userId, e);
            throw e;
        }
    }

    public void markNotificationsAsRead(UUID userId) {
        try {
            logger.info("Marking notifications as read for user with ID: {}", userId);
            List<Notifica> unreadNotifiche = notificaRepository.findByUserIdNotAndReadFalse(userId);
            for (Notifica notifica : unreadNotifiche) {
                notifica.setRead(true);
                notificaRepository.save(notifica);
            }
        } catch (Exception e) {
            logger.error("Error marking notifications as read for user with ID: {}", userId, e);
            throw e;
        }
    }

    public Notifica createNotifica(Notifica notifica) {
        try {
            logger.info("Saving new notification to the database: {}", notifica);
            return notificaRepository.save(notifica);
        } catch (Exception e) {
            logger.error("Error saving notification to the database", e);
            throw e;
        }
    }

    public Notifica getNotificaById(int id) {
        try {
            logger.info("Fetching notification with ID: {}", id);
            return notificaRepository.findById(id).orElse(null);
        } catch (Exception e) {
            logger.error("Error fetching notification with ID: {}", id, e);
            throw e;
        }
    }

    public void deleteNotifica(int id) {
        try {
            logger.info("Deleting notification with ID: {}", id);
            notificaRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting notification with ID: {}", id, e);
            throw e;
        }
    }

    public void createAziendaAddedNotification(User user, Azienda azienda) {
        Notifica notifica = new Notifica();
        String messaggio = String.format("%s ha aggiunto un nuovo annuncio: %s",
                user.getName() + " " + user.getSurname(), azienda.getName());
        notifica.setMessaggio(messaggio);
        notifica.setTipo("azienda");
        notifica.setTitolo("Nuovo Annuncio");
        notifica.setUrl("/Aziende");
        notifica.setUserId(user.getId());
        notifica.setFonte(user.getName() + " " + user.getSurname());
        notifica.setTimestamp(System.currentTimeMillis()); // Aggiungi il timestamp
        String avatarUrl = user.getAvatarURL();
        System.out.println("User Avatar URL: " + avatarUrl);
        if (avatarUrl != null && avatarUrl.contains("cloudinary")) {
            notifica.setAvatarURL(avatarUrl);
        }
        notificaRepository.save(notifica);
    }

    public void createNewsNotification(String title, String message, String url) {
        Notifica notifica = new Notifica();
        notifica.setTitolo(title);
        notifica.setMessaggio(message);
        notifica.setTipo("news");
        notifica.setUrl(url);
        createNotifica(notifica);
    }

    public void createProductAddedNotification(User user, Product product) {
        Notifica notifica = new Notifica();
        String categoria = product.getCategoria();
        String url = "";

        switch (categoria.toLowerCase()) {
            case "piantine":
                url = "/Piantine";
                break;
            case "artigianali":
                url = "/Artigianali";
                break;
            case "animali":
                url = "/Animali";
                break;
            case "attrezzature":
                url = "/Attrezzature";
                break;
            default:
                url = "/products";
        }

        String messaggio = String.format("%s ha aggiunto un nuovo prodotto, categoria: %s.",
                user.getName() + " " + user.getSurname(), categoria);
        notifica.setMessaggio(messaggio);
        notifica.setTipo("prodotto");
        notifica.setTitolo("Nuovo Prodotto");
        notifica.setUrl(url);
        notifica.setUserId(user.getId());
        notifica.setFonte(user.getName() + " " + user.getSurname());
        notifica.setTimestamp(System.currentTimeMillis()); // Aggiungi il timestamp
        String avatarUrl = user.getAvatarURL();
        System.out.println("User Avatar URL: " + avatarUrl);
        if (avatarUrl != null && avatarUrl.contains("cloudinary")) {
            notifica.setAvatarURL(avatarUrl);
        }
        notificaRepository.save(notifica);
    }
}

















