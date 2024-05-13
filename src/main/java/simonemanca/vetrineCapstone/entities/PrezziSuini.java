package simonemanca.vetrineCapstone.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "prezzi_suini")
public class PrezziSuini {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prodotto;
    private Double prezzo;
    private String luogo;

    @Temporal(TemporalType.DATE)
    private Date data;

    private String variazionePerc;

    // Constructors
    public PrezziSuini() {}

    public PrezziSuini(String prodotto, Double prezzo, String luogo, Date data, String variazionePerc) {
        this.prodotto = prodotto;
        this.prezzo = prezzo;
        this.luogo = luogo;
        this.data = data;
        this.variazionePerc = variazionePerc;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProdotto() {
        return prodotto;
    }

    public void setProdotto(String prodotto) {
        this.prodotto = prodotto;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getVariazionePerc() {
        return variazionePerc;
    }

    public void setVariazionePerc(String variazionePerc) {
        this.variazionePerc = variazionePerc;
    }
}