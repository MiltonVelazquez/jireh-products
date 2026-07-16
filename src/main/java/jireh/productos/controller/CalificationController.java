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

import jireh.productos.models.CalificationEntity;
import jireh.productos.repositories.CalificationRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping(path = "products/calification")
public class CalificationController {

    @Autowired
    private CalificationRepository calificationRepository;


    @PostMapping
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CalificationEntity> save(
            @RequestBody CalificationEntity calificationEntity, 
            @AuthenticationPrincipal Jwt principal) {
        
        Long userId = Long.valueOf(principal.getSubject());
        calificationEntity.setUserId(userId);
        
        CalificationEntity saved = calificationRepository.save(calificationEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);    
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable @NonNull Long id, 
            @AuthenticationPrincipal Jwt principal) {
        
        Long currentUserId = Long.valueOf(principal.getSubject());
        boolean isAdmin = principal.getClaimAsStringList("roles").contains("ADMIN");

        CalificationEntity calification = calificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La calificación con ID " + id + " no existe."));

        if (isAdmin || calification.getUserId().equals(currentUserId)) {
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
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Iterable<CalificationEntity>> getAll() {
        return ResponseEntity.ok(calificationRepository.findAll());
    }
}