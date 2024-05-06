package simonemanca.vetrineCapstone.payloads;

import java.util.Date;

public record UpdatePriceHistoryDTO(
        String prodotto,
        Double prezzo,
        String luogo,
        Date data,
        String variazionePerc
) {}
