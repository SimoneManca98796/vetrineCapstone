package simonemanca.vetrineCapstone.entities;

import jakarta.persistence.*;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private int rating;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    // Getters and setters
}
