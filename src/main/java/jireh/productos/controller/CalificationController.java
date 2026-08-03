package jireh.productos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
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

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping(path = "products/calification")
public class CalificationController {

    @Autowired
    private CalificationRepository calificationRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CalificationEntity> save(@RequestBody ReviewDTO dto) {
        ProductEntity product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + dto.getProductId()));

        CalificationEntity calificationEntity = new CalificationEntity();
        calificationEntity.setProduct(product);
        calificationEntity.setUserId(dto.getUserId());
        calificationEntity.setDescription(dto.getComment());
        calificationEntity.setScore(Double.valueOf(dto.getRating()));

        CalificationEntity saved = calificationRepository.save(calificationEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @NonNull Long id, 
            @AuthenticationPrincipal Jwt principal) {

        CalificationEntity calification = calificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La calificación con ID " + id + " no existe."));

        // Comprobación opcional de roles si el token está presente
        boolean isAdmin = principal != null 
                && principal.getClaimAsStringList("roles") != null 
                && principal.getClaimAsStringList("roles").contains("ADMIN");

        if (isAdmin || calification != null) {
            calificationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new org.springframework.security.access.AccessDeniedException("No tienes permisos para eliminar esta reseña.");
        }
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
