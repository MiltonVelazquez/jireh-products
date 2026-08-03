package jireh.productos.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import jireh.productos.dto.ReviewDTO;
import jireh.productos.models.CalificationEntity;
import jireh.productos.models.ProductEntity;
import jireh.productos.repositories.CalificationRepository;
import jireh.productos.repositories.ProductRepository;

@RestController
@RequestMapping("/products/calification")
public class CalificationController {

    @Autowired
    private CalificationRepository calificationRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<ReviewDTO> save(@RequestBody ReviewDTO dto) {
        ProductEntity product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + dto.productId()));

        CalificationEntity calificationEntity = new CalificationEntity();
        calificationEntity.setProduct(product);
        calificationEntity.setUserId(dto.userId());
        calificationEntity.setDescription(dto.comment());
        
        if (dto.rating() != null) {
            calificationEntity.setScore(Double.valueOf(dto.rating()));
        }

        CalificationEntity saved = calificationRepository.save(calificationEntity);

        // Instanciado respetando el orden exacto de ReviewDTO:
        // (id, userId, userName, productId, rating, comment, createdAt)
        ReviewDTO responseDto = new ReviewDTO(
            saved.getId(),
            saved.getUserId(),
            dto.userName() != null ? dto.userName() : "Usuario",
            saved.getProduct().getId(),
            saved.getScore() != null ? saved.getScore().intValue() : null,
            saved.getDescription(),
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @NonNull Long id, 
            @AuthenticationPrincipal Jwt principal) {

        CalificationEntity calification = calificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La calificación con ID " + id + " no existe."));

        calificationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDTO>> getByProduct(@PathVariable Long productId) {
        List<CalificationEntity> entities = calificationRepository.findByProductId(productId);
        
        // Mapeo respetando el orden exacto del record
        List<ReviewDTO> dtos = entities.stream()
                .map(c -> new ReviewDTO(
                        c.getId(),
                        c.getUserId(),
                        "Usuario",
                        c.getProduct().getId(),
                        c.getScore() != null ? c.getScore().intValue() : null,
                        c.getDescription(),
                        LocalDateTime.now()
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }
}
