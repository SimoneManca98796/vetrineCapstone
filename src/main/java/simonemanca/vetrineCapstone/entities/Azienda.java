package simonemanca.vetrineCapstone.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "aziende")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Azienda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String tipo;
    private String contatto;  // Può essere telefono o email
    private String dettagli;
    private String categoria; // "Richiedente" o "Proponente"
    private UUID userId;
}



