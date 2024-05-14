package simonemanca.vetrineCapstone.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import simonemanca.vetrineCapstone.entities.Product;
import simonemanca.vetrineCapstone.entities.ProductDTO;
import simonemanca.vetrineCapstone.services.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @GetMapping("/categoryName/{categoryName}")
    public List<ProductDTO> getProductsByCategoryName(@PathVariable String categoryName) {
        return productService.getProductsByCategoryName(categoryName);
    }

    @PostMapping("/categoryName/{categoryName}")
    public ResponseEntity<ProductDTO> createProduct(
            @PathVariable String categoryName,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("file") MultipartFile file) {

        ProductDTO createdProduct = productService.createProduct(name, description, price, categoryName, file);
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
            System.out.println("Uno o più parametri non sono stati inviati correttamente.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File e nome sono obbligatori.");
        }

        System.out.println("File: " + file.getOriginalFilename());
        System.out.println("Name: " + name);
        return ResponseEntity.ok("File caricato con successo");
    }
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) {
        System.out.println("Creating product: " + productDTO.getName());

        ProductDTO savedProduct = productService.saveProduct(productDTO);
        return ResponseEntity.ok(savedProduct);
    }
}




