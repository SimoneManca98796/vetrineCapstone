package simonemanca.vetrineCapstone.entities;

import jakarta.persistence.*;
import lombok.*;

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
}



