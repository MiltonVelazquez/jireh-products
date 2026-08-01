package jireh.productos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jireh.productos.dto.ProductDTO;
import jireh.productos.models.WishListEntity;
import jireh.productos.repositories.WishListRepository;
import jireh.productos.services.ProductService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping(path = "products/wishlist")
public class WishListController {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private ProductService productService;

    private Long extractUserId(Jwt principal) {
        if (principal == null) {
            throw new IllegalArgumentException("No se proporcionó token de autenticación");
        }
        
        Object idClaim = principal.getClaim("id");
        if (idClaim == null) {
            idClaim = principal.getClaim("userId");
        }

        if (idClaim instanceof Number) {
            return ((Number) idClaim).longValue();
        } else if (idClaim instanceof String) {
            return Long.valueOf((String) idClaim);
        }

        String subject = principal.getSubject();
        try {
            return Long.valueOf(subject);
        } catch (NumberFormatException e) {
            throw new RuntimeException("El token JWT no contiene una claim 'id' válida para el usuario");
        }
    }

    @PostMapping
    public ResponseEntity<ProductDTO> save(@RequestBody WishListEntity wishListEntity, @AuthenticationPrincipal Jwt principal){
        Long userId = extractUserId(principal);
        wishListEntity.setUserId(userId);

        WishListEntity saved = wishListRepository.save(wishListEntity);
        return ResponseEntity.status(201).body(productService.convertToDTO(saved.getProduct()));
    }

    @DeleteMapping("/deleteWishList/{id}")
    public ResponseEntity<Object> delete(@PathVariable @NonNull Long id, @AuthenticationPrincipal Jwt principal){
        Long userId = extractUserId(principal);
        Optional<WishListEntity> item = wishListRepository.findById(id);
    
        if (item.isPresent() && item.get().getUserId().equals(userId)) {
            wishListRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getUserWishList(@AuthenticationPrincipal Jwt principal) {
        Long userId = extractUserId(principal);
        List<WishListEntity> wishList = wishListRepository.findByUserId(userId);
        
        List<ProductDTO> products = wishList.stream()
                .map(item -> productService.convertToDTO(item.getProduct())) 
                .toList();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WishListEntity> getById(@PathVariable("id") Long id){
        return wishListRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> deleteByProductId(@PathVariable Long productId, @AuthenticationPrincipal Jwt principal) {
        Long userId = extractUserId(principal);

        WishListEntity wishListItem = wishListRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("El producto no se encuentra en tu lista de deseos"));

        wishListRepository.delete(wishListItem);

        return ResponseEntity.noContent().build();
    }
}
