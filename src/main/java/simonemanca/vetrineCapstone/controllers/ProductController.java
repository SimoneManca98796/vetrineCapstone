package simonemanca.vetrineCapstone.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import simonemanca.vetrineCapstone.entities.ProductDTO;
import simonemanca.vetrineCapstone.services.ProductService;

import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import simonemanca.vetrineCapstone.entities.User;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @GetMapping("/categoryName/{categoryName}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategoryName(@PathVariable String categoryName) {
        List<ProductDTO> products = productService.getProductsByCategoryName(categoryName);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam("query") String query) {
        List<ProductDTO> products = productService.searchProducts(query);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/categoryName/{categoryName}")
    public ResponseEntity<ProductDTO> createProduct(
            @PathVariable String categoryName,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("imageUrl") String imageUrl,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        ProductDTO createdProduct = productService.createProduct(user, name, description, price, categoryName, imageUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadProduct(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) {
        if (file == null || name == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File e nome sono obbligatori.");
        }

        String imageUrl = productService.uploadFile(file, name);
        return ResponseEntity.ok(Collections.singletonMap("imageUrl", imageUrl));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO, @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        ProductDTO savedProduct = productService.saveProduct(user, productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }
}










