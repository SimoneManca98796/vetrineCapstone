package simonemanca.vetrineCapstone.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${stripe.api.publishable.key}")
    private String publishableKey;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody Map<String, Object> data) {
        logger.info("Ricevuta richiesta per creare un PaymentIntent con i dati: {}", data);

        try {
            int amount = (int) data.get("amount");
            logger.info("Importo del pagamento: {}", amount);

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) amount)
                    .setCurrency("eur")
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            logger.info("PaymentIntent creato con successo: {}", paymentIntent.getId());

            Map<String, String> responseData = new HashMap<>();
            responseData.put("clientSecret", paymentIntent.getClientSecret());

            logger.info("Client secret generato: {}", paymentIntent.getClientSecret());
            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            logger.error("Errore durante la creazione del PaymentIntent", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        } catch (Exception e) {
            logger.error("Errore imprevisto durante la creazione del PaymentIntent", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Errore imprevisto: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/publishable-key")
    public ResponseEntity<Map<String, String>> getPublishableKey() {
        logger.info("Richiesta della chiave pubblicabile");

        Map<String, String> response = new HashMap<>();
        response.put("publishableKey", publishableKey);

        logger.info("Chiave pubblicabile fornita: {}", publishableKey);
        return ResponseEntity.ok(response);
    }
}







