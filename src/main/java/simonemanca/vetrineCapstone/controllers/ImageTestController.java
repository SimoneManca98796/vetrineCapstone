package simonemanca.vetrineCapstone.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ImageTestController {

    @GetMapping("/test-image")
    public ResponseEntity<Resource> getImage() {
        Path filePath = Paths.get("uploads", "L.png");
        Resource resource = new FileSystemResource(filePath);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Modifica il MediaType in base al tipo di immagine che stai testando
                .body(resource);
    }
}

