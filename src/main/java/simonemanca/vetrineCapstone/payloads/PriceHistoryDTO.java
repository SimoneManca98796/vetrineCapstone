package simonemanca.vetrineCapstone.payloads;

import java.util.Date;

public record PriceHistoryDTO(
        Long id,
        String prodotto,
        Double prezzo,
        String luogo,
        Date data,
        String variazionePerc
) {}
