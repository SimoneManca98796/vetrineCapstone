package simonemanca.vetrineCapstone.tools;

import kong.unirest.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import simonemanca.vetrineCapstone.entities.User;

import java.util.Collections;

@Component
public class SendGridSender {
    private String apiKey;
    private String fromEmail;

    public SendGridSender(@Value("${sendgrid.apikey}") String apiKey, @Value("${sendgrid.fromemail}") String fromEmail){
        this.apiKey = apiKey;
        this.fromEmail = fromEmail;
    }

    public void sendRegistrationEmail(User recipient){
        String apiUrl = "https://api.sendgrid.com/v3/mail/send";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + this.apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject body = new JSONObject();
        body.put("personalizations", Collections.singletonList(new JSONObject()
                .put("to", Collections.singletonList(new JSONObject()
                        .put("email", recipient.getEmail())))));
        body.put("from", new JSONObject()
                .put("email", this.fromEmail));
        body.put("subject", "Registrazione Completata!");
        body.put("content", Collections.singletonList(new JSONObject()
                .put("type", "text/plain")
                .put("value", "Complimenti " + recipient.getName() + ", sei stato registrato con successo su VetrineCapstone!")));

        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
        System.out.println(response.getBody());
    }
}

