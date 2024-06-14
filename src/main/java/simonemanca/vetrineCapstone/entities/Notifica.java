package simonemanca.vetrineCapstone.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

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

    @Column(nullable = false)
    private UUID userId;

    @Column(name = "user_avatar")
    private String avatarURL;

    @Column(nullable = false)
    private long timestamp;

    public Notifica(String titolo, String messaggio, String tipo, String url, boolean read) {
        this.titolo = titolo;
        this.messaggio = messaggio;
        this.tipo = tipo;
        this.url = url;
        this.read = read;
        this.timestamp = System.currentTimeMillis();
    }
}







