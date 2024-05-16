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

import simonemanca.vetrineCapstone.entities.PrezziBovini;
import simonemanca.vetrineCapstone.entities.PrezziOvini;
import simonemanca.vetrineCapstone.repositories.PrezziBoviniRepository;
import simonemanca.vetrineCapstone.repositories.PrezziOviniRepository;

@Service
public class PrezziBoviniService {

    @Autowired
    private PrezziBoviniRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(PrezziOviniService.class);

    // Tutti i prezzi degli ovini con paginazione
    public Page<PrezziBovini> getAllPrezzi(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // Metodo per filtrare per data e luogo con paginazione
    public Page<PrezziBovini> findByDataAndLuogo(String dataStr, String luogo, Pageable pageable) {
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
    public Optional<PrezziBovini> getPrezzoById(Long id) {
        return repository.findById(id);
    }

    // Salva un nuovo prezzo di ovini
    public PrezziBovini savePrezzo(PrezziBovini prezzo) {
        return repository.save(prezzo);
    }

    // Aggiorna un prezzo di ovini esistente
    public PrezziBovini updatePrezzo(Long id, PrezziBovini prezzo) {
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
