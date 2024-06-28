package simonemanca.vetrineCapstone.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import simonemanca.vetrineCapstone.entities.Category;
import simonemanca.vetrineCapstone.entities.Product;
import simonemanca.vetrineCapstone.entities.ProductDTO;
import simonemanca.vetrineCapstone.repositories.CategoryRepository;
import simonemanca.vetrineCapstone.repositories.ProductRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import simonemanca.vetrineCapstone.entities.User;
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private NotificaService notificaService;

    private final Cloudinary cloudinary;

    @Autowired
    public ProductService(
            @Value("${cloudinary.name}") String cloudName,
            @Value("${cloudinary.key}") String apiKey,
            @Value("${cloudinary.secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public List<ProductDTO> getProductsByCategoryName(String categoryName) {
        return productRepository.findByCategoryName(categoryName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO createProduct(User user, String name, String description, double price, String categoryName, String imageUrl) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Categoria non trovata"));

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageUrl(imageUrl);
        product.setCategory(category);
        product.setUser(user);

        product = productRepository.save(product);

        // Creare una notifica
        notificaService.createProductAddedNotification(user, product);

        return convertToDTO(product);
    }

    public ProductDTO saveProduct(User user, ProductDTO productDTO) {
        Category category = categoryRepository.findByName(productDTO.getCategoryName())
                .orElseThrow(() -> new RuntimeException("Categoria non trovata"));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setImageUrl(productDTO.getImageUrl());
        product.setCategory(category);
        product.setUser(user); // Imposta il venditore

        product = productRepository.save(product);

        // Creare una notifica
        notificaService.createProductAddedNotification(user, product);

        return convertToDTO(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public String uploadFile(MultipartFile file, String name) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("public_id", name));
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il salvataggio dell'immagine", e);
        }
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getUser() != null ? product.getUser().getName() : null,
                product.getUser() != null ? product.getUser().getSurname() : null
        );
        return productDTO;
    }
}













