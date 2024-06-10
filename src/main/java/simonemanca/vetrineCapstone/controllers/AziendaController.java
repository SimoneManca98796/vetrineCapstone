package simonemanca.vetrineCapstone.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import simonemanca.vetrineCapstone.entities.Azienda;
import simonemanca.vetrineCapstone.entities.User;
import simonemanca.vetrineCapstone.exceptions.UnauthorizedException;
import simonemanca.vetrineCapstone.services.AziendaService;
import simonemanca.vetrineCapstone.exceptions.NotFoundException;
import simonemanca.vetrineCapstone.exceptions.BadRequestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/aziende")
public class AziendaController {

    private static final Logger logger = LoggerFactory.getLogger(AziendaController.class);

    @Autowired
    private AziendaService aziendaService;

    @GetMapping
    public ResponseEntity<List<Azienda>> getAllAziende() {
        List<Azienda> aziende = aziendaService.getAllAziende();
        return ResponseEntity.ok(aziende);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Azienda> getAziendaById(@PathVariable Long id) {
        Azienda azienda = aziendaService.getAziendaById(id);
        if (azienda == null) {
            throw new NotFoundException("Azienda non trovata con ID: " + id);
        }
        return ResponseEntity.ok(azienda);
    }

    @PostMapping
    public ResponseEntity<Azienda> createAzienda(@RequestBody Azienda azienda, @AuthenticationPrincipal User user) {
        logger.info("Dati ricevuti: " + azienda.toString()); // Log dei dati ricevuti

        if (azienda.getName() == null || azienda.getDescription() == null || azienda.getTipo() == null) {
            throw new BadRequestException("I campi nome, descrizione e tipo sono obbligatori.");
        }
        if (azienda.getContatto() == null || azienda.getContatto().isEmpty()) {
            throw new BadRequestException("È necessario fornire un contatto (telefono o email).");
        }

        // Imposta l'ID dell'utente corrente
        azienda.setUserId(user.getId());
        logger.info("User ID: " + user.getId()); // Log dell'ID utente
        Azienda createdAzienda = aziendaService.saveAzienda(user, azienda); // Passa l'oggetto User
        logger.info("Azienda creata con ID utente: " + azienda.getUserId()); // Log dell'azienda creata con ID utente
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAzienda);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAzienda(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Azienda azienda = aziendaService.getAziendaById(id);
        if (azienda == null) {
            throw new NotFoundException("Azienda non trovata con ID: " + id);
        }

        // Verifica se l'utente è l'autore dell'annuncio o un admin
        if (!azienda.getUserId().equals(user.getId()) && !user.getRole().equals("ADMIN")) {
            throw new UnauthorizedException("Non sei autorizzato a eliminare questo annuncio.");
        }

        aziendaService.deleteAzienda(id);
        return ResponseEntity.noContent().build();
    }
}



















