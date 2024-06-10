package simonemanca.vetrineCapstone.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simonemanca.vetrineCapstone.entities.Azienda;
import simonemanca.vetrineCapstone.repositories.AziendaRepository;

import java.util.List;
import simonemanca.vetrineCapstone.entities.User;
@Service
public class AziendaService {

    @Autowired
    private AziendaRepository aziendaRepository;

    @Autowired
    private NotificaService notificaService;

    public List<Azienda> getAllAziende() {
        return aziendaRepository.findAll();
    }

    public Azienda getAziendaById(Long id) {
        return aziendaRepository.findById(id).orElse(null);
    }

    public Azienda saveAzienda(User user, Azienda azienda) {
        Azienda savedAzienda = aziendaRepository.save(azienda);

        // Creare una notifica
        notificaService.createAziendaAddedNotification(user, savedAzienda);

        return savedAzienda;
    }

    public void deleteAzienda(Long id) {
        aziendaRepository.deleteById(id);
    }
}