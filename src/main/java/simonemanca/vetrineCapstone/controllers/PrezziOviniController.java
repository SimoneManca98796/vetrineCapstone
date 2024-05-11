package simonemanca.vetrineCapstone.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simonemanca.vetrineCapstone.entities.PrezzoStorico;
import simonemanca.vetrineCapstone.services.PrezzoStoricoService;

import java.util.Optional;

@RestController
@RequestMapping("/api/prezziOvini")
public class PrezziOviniController {

    @Autowired
    private PrezzoStoricoService prezzoStoricoService;

    // Ottieni tutti i prezzi storici con paginazione e gestione eccezioni
    @GetMapping
    public ResponseEntity<?> getAllPrezzi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "data") String sortBy,
            @RequestParam(required = false) String data,
            @RequestParam(required = false) String luogo) {
        System.out.println("Data ricevuta: " + data);  // Stampa il valore di data ricevuto
        System.out.println("Luogo ricevuto: " + luogo);  // Stampa il valore di luogo ricevuto

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
            Page<PrezzoStorico> prezzi = prezzoStoricoService.findByDataAndLuogo(data, luogo, pageable);
            return ResponseEntity.ok(prezzi);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel server: " + e.getMessage());
        }
    }

    // Ottiene un prezzo storico per ID con gestione eccezioni
    @GetMapping("/{id}")
    public ResponseEntity<?> getPrezzoById(@PathVariable Long id) {
        try {
            Optional<PrezzoStorico> prezzoOptional = prezzoStoricoService.getPrezzoById(id);
            return prezzoOptional.map(prezzo -> ResponseEntity.ok(prezzo))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel recuperare il prezzo: " + e.getMessage());
        }
    }

    // Crea un nuovo record di prezzo storico con gestione eccezioni
    @PostMapping
    public ResponseEntity<?> createPrezzo(@RequestBody PrezzoStorico prezzo) {
        try {
            PrezzoStorico savedPrezzo = prezzoStoricoService.savePrezzo(prezzo);
            return new ResponseEntity<>(savedPrezzo, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nella creazione del prezzo: " + e.getMessage());
        }
    }

    // Aggiorna un record di prezzo storico esistente con gestione eccezioni
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrezzo(@PathVariable Long id, @RequestBody PrezzoStorico prezzo) {
        try {
            PrezzoStorico updatedPrezzo = prezzoStoricoService.updatePrezzo(id, prezzo);
            return updatedPrezzo != null ? ResponseEntity.ok(updatedPrezzo) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nell'aggiornamento del prezzo: " + e.getMessage());
        }
    }

    // Elimina un record di prezzo storico con gestione eccezioni
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrezzo(@PathVariable Long id) {
        try {
            prezzoStoricoService.deletePrezzo(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nell'eliminazione del prezzo: " + e.getMessage());
        }
    }
}
