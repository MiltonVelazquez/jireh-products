package jireh.productos.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "calificacion")
public class CalificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "product_id")
    private Long productId;

    @NotBlank
    @Column(name = "user_id")
    private Long userId;

    @NotBlank
    @Column(name = "description")
    private String description;

    @NotBlank
    @Column(name = "score")
    private Double score;

}
