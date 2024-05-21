package simonemanca.vetrineCapstone.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simonemanca.vetrineCapstone.entities.Azienda;
import simonemanca.vetrineCapstone.repositories.AziendaRepository;

import java.util.List;

@Service
public class AziendaService {

    @Autowired
    private AziendaRepository aziendaRepository;

    public List<Azienda> getAllAziende() {
        return aziendaRepository.findAll();
    }

    public Azienda getAziendaById(Long id) {
        return aziendaRepository.findById(id).orElse(null);
    }

    public Azienda saveAzienda(Azienda azienda) {
        return aziendaRepository.save(azienda);
    }

    public void deleteAzienda(Long id) {
        aziendaRepository.deleteById(id);
    }
}



