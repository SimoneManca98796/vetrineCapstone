package simonemanca.vetrineCapstone.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notifiche")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Notifica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String titolo;
    private String messaggio;
    private String tipo;
    private String url;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean read;

    private String fonte;

    public Notifica(String titolo, String messaggio, String tipo, String url, boolean read) {
        this.titolo = titolo;
        this.messaggio = messaggio;
        this.tipo = tipo;
        this.url = url;
        this.read = read;
    }
}




