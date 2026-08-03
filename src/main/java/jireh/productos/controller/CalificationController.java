package jireh.productos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jireh.productos.dto.ReviewDTO;
import jireh.productos.models.CalificationEntity;
import jireh.productos.models.ProductEntity;
import jireh.productos.repositories.CalificationRepository;
import jireh.productos.repositories.ProductRepository;

@RestController
@RequestMapping(path = "products/calification")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class CalificationController {

    @Autowired
    private CalificationRepository calificationRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CalificationEntity> save(@RequestBody ReviewDTO dto) {
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
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @NonNull Long id, 
            @AuthenticationPrincipal Jwt principal) {

        CalificationEntity calification = calificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La calificación con ID " + id + " no existe."));

        boolean isAdmin = principal != null 
                && principal.getClaimAsStringList("roles") != null 
                && principal.getClaimAsStringList("roles").contains("ADMIN");

        calificationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CalificationEntity>> getByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(calificationRepository.findByProductId(productId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalificationEntity> getById(@PathVariable("id") @NonNull Long id) {
        CalificationEntity calification = calificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La calificación con ID " + id + " no fue encontrada."));
        return ResponseEntity.ok(calification);
    }

    @GetMapping
    public ResponseEntity<Iterable<CalificationEntity>> getAll() {
        return ResponseEntity.ok(calificationRepository.findAll());
    }
}
