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

@Service
public class NotificaService {

    @Autowired
    private NotificaRepository notificaRepository;

    private static final Logger logger = LoggerFactory.getLogger(NotificaService.class);

    public List<Notifica> getAllNotifiche() {
        try {
            logger.info("Fetching all notifications from the database");
            return notificaRepository.findAll();
        } catch (Exception e) {
            logger.error("Error fetching notifications from the database", e);
            throw e;
        }
    }

    public List<Notifica> getUnreadNotifiche() {
        try {
            logger.info("Fetching unread notifications from the database");
            return notificaRepository.findByReadFalse();
        } catch (Exception e) {
            logger.error("Error fetching unread notifications from the database", e);
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
        notifica.setMessaggio(String.format("Utente %s ha aggiunto un nuovo annuncio: %s.",
                user.getName(), azienda.getName()));
        notifica.setTipo("azienda");
        notifica.setTitolo("Nuovo Annuncio");
        notifica.setUrl("/aziende/" + azienda.getId());
        notifica.setUserId(user.getId());  // Imposta l'ID utente
        createNotifica(notifica);
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
        notifica.setMessaggio(String.format("Utente %s ha aggiunto un nuovo prodotto: %s.",
                user.getName(), product.getName()));
        notifica.setTipo("prodotto");
        notifica.setTitolo("Nuovo Prodotto");
        notifica.setUrl("/products/" + product.getId());
        notifica.setUserId(user.getId());  // Imposta l'ID utente
        createNotifica(notifica);
    }
}

