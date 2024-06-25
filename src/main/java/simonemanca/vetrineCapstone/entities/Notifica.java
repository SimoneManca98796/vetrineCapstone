package simonemanca.vetrineCapstone.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
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
    private String fonte;

    @Column(nullable = false)
    private UUID userId;

    @Column(name = "user_avatar")
    private String avatarURL;

    @Column(nullable = false)
    private long timestamp;

    @ElementCollection
    @CollectionTable(name = "notifica_readers", joinColumns = @JoinColumn(name = "notifica_id"))
    @Column(name = "reader_id")
    private Set<UUID> readers = new HashSet<>();

    public void addReader(UUID userId) {
        this.readers.add(userId);
    }

    public boolean isReadBy(UUID userId) {
        return this.readers.contains(userId);
    }

    public Notifica(String titolo, String messaggio, String tipo, String url, String fonte, UUID userId, String avatarURL) {
        this.titolo = titolo;
        this.messaggio = messaggio;
        this.tipo = tipo;
        this.url = url;
        this.fonte = fonte;
        this.userId = userId;
        this.avatarURL = avatarURL;
        this.timestamp = System.currentTimeMillis();
    }
}









