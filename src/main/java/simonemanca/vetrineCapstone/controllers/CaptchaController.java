package simonemanca.vetrineCapstone.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class CaptchaController {

    @Value("${hcaptcha.secret}")
    private String hcaptchaSecret;

    @PostMapping("/api/verify-captcha")
    public ResponseEntity<?> verifyCaptcha(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String url = "https://hcaptcha.com/siteverify?secret=" + hcaptchaSecret + "&response=" + token;

        RestTemplate restTemplate = new RestTemplate();
        Map response = restTemplate.postForObject(url, null, Map.class);

        return ResponseEntity.ok(response);
    }
}



