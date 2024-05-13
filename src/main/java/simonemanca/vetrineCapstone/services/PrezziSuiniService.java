package simonemanca.vetrineCapstone.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import simonemanca.vetrineCapstone.entities.PrezziSuini;
import simonemanca.vetrineCapstone.repositories.PrezziSuiniRepository;

@Service
public class PrezziSuiniService {

    @Autowired
    private PrezziSuiniRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(PrezziSuiniService.class);

    // Tutti i prezzi degli ovini con paginazione
    public Page<PrezziSuini> getAllPrezzi(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // Metodo per filtrare per data e luogo con paginazione
    public Page<PrezziSuini> findByDataAndLuogo(String dataStr, String luogo, Pageable pageable) {
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

    // Singolo prezzo di ovini per ID
    public Optional<PrezziSuini> getPrezzoById(Long id) {
        return repository.findById(id);
    }

    // Salva un nuovo prezzo di ovini
    public PrezziSuini savePrezzo(PrezziSuini prezzo) {
        return repository.save(prezzo);
    }

    // Aggiorna un prezzo di ovini esistente
    public PrezziSuini updatePrezzo(Long id, PrezziSuini prezzo) {
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

    // Cancella un prezzo di ovini
    public void deletePrezzo(Long id) {
        repository.deleteById(id);
    }
}

