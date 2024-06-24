package simonemanca.vetrineCapstone.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import simonemanca.vetrineCapstone.entities.User;
import kong.unirest.json.JSONObject;

import java.util.Collections;

@Service
public class EmailService {

    @Value("${sendgrid.apikey}")
    private String apiKey;

    @Value("${sendgrid.fromemail}")
    private String fromEmail;

    @Value("${frontend.reset-password-url}")
    private String resetPasswordUrl;

    public void sendRegistrationEmail(User user) {
        String url = "https://api.sendgrid.com/v3/mail/send";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        JSONObject requestBody = new JSONObject();
        requestBody.put("personalizations", Collections.singletonList(new JSONObject()
                .put("to", Collections.singletonList(new JSONObject().put("email", user.getEmail())))));
        requestBody.put("from", new JSONObject().put("email", fromEmail));
        requestBody.put("subject", "Benvenuto su VetrineCapstone!");
        requestBody.put("content", Collections.singletonList(new JSONObject()
                .put("type", "text/plain")
                .put("value", "Ciao " + user.getName() + ",\n\nGrazie per esserti registrato su VetrineCapstone!")));

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            System.out.println("Response from SendGrid: " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPasswordResetEmail(User user) {
        String url = "https://api.sendgrid.com/v3/mail/send";

        String resetLink = resetPasswordUrl + "?token=" + user.getResetToken();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        JSONObject requestBody = new JSONObject();
        requestBody.put("personalizations", Collections.singletonList(new JSONObject()
                .put("to", Collections.singletonList(new JSONObject().put("email", user.getEmail())))));
        requestBody.put("from", new JSONObject().put("email", fromEmail));
        requestBody.put("subject", "Recupero Password VetrineCapstone");
        requestBody.put("content", Collections.singletonList(new JSONObject()
                .put("type", "text/plain")
                .put("value", "Ciao " + user.getName() + ",\n\nPer favore clicca sul link seguente per reimpostare la tua password: " + resetLink)));

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            System.out.println("Response from SendGrid: " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



