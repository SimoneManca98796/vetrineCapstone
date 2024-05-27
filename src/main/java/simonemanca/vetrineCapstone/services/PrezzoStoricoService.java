package simonemanca.vetrineCapstone.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import simonemanca.vetrineCapstone.entities.PrezzoStorico;
import simonemanca.vetrineCapstone.repositories.PrezzoStoricoRepository;

@Service
public class PrezzoStoricoService {

    @Autowired
    private PrezzoStoricoRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(PrezzoStoricoService.class);

    // Tutti i prezzi storici con paginazione
    public Page<PrezzoStorico> getAllPrezzi(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // Metodo per filtrare per data e luogo con paginazione
    public Page<PrezzoStorico> findByDataAndLuogo(String dataStr, String luogo, Pageable pageable) {
        Date data = null;
        try {
            if (dataStr != null && !dataStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                data = sdf.parse(dataStr);
                logger.info("Data passata alla query: " + data);
            }
            return repository.findByDataAndLuogo(data, luogo, pageable);
        } catch (Exception e) {
            logger.error("Errore nella conversione della data: " + dataStr, e);
            throw new IllegalArgumentException("Formato data non valido: " + dataStr, e);
        }
    }

    // Metodo per filtrare per luogo con paginazione
    public Page<PrezzoStorico> findByLuogo(String luogo, Pageable pageable) {
        return repository.findByLuogo(luogo, pageable);
    }

    // Singolo prezzo storico per ID
    public Optional<PrezzoStorico> getPrezzoById(Long id) {
        return repository.findById(id);
    }

    // Salva un nuovo prezzo storico
    public PrezzoStorico savePrezzo(PrezzoStorico prezzo) {
        return repository.save(prezzo);
    }

    // Aggiorna un prezzo storico esistente
    public PrezzoStorico updatePrezzo(Long id, PrezzoStorico prezzo) {
        return repository.findById(id)
                .map(existingPrezzo -> {
                    existingPrezzo.setProdotto(prezzo.getProdotto());
                    existingPrezzo.setPrezzo(prezzo.getPrezzo());
                    existingPrezzo.setLuogo(prezzo.getLuogo());
                    existingPrezzo.setData(prezzo.getData());
                    existingPrezzo.setVariazionePerc(prezzo.getVariazionePerc());
                    return repository.save(existingPrezzo);
                })
                .orElseThrow(() -> new RuntimeException("Prezzo non trovato con ID: " + id));
    }

    // Cancella un prezzo storico
    public void deletePrezzo(Long id) {
        repository.deleteById(id);
    }
}






