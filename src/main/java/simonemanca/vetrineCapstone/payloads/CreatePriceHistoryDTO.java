package simonemanca.vetrineCapstone.payloads;

import java.util.Date;

public record CreatePriceHistoryDTO(
        String prodotto,
        Double prezzo,
        String luogo,
        Date data,
        String variazionePerc
) {}
