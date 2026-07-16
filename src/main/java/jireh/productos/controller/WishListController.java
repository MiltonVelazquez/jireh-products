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

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping(path = "products/wishlist")
public class WishListController {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private ProductService productService;

    @PostMapping
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<ProductDTO> save(@RequestBody WishListEntity wishListEntity, @AuthenticationPrincipal Jwt principal){
        Long userId = Long.valueOf(principal.getSubject());
        wishListEntity.setUserId(userId);

        WishListEntity saved = wishListRepository.save(wishListEntity);
    
        // Devolvemos el DTO para que el frontend tenga toda la info del producto recién añadido
        return ResponseEntity.status(201).body(productService.convertToDTO(saved.getProduct()));
    }

    @DeleteMapping("/deleteWishList/{id}")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> delete(@PathVariable @NonNull Long id, @AuthenticationPrincipal Jwt principal){
        Long userId = Long.valueOf(principal.getSubject());
        Optional<WishListEntity> item = wishListRepository.findById(id);
    
        if (item.isPresent() && item.get().getUserId().equals(userId)) {
            wishListRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ProductDTO>> getUserWishList(@AuthenticationPrincipal Jwt principal) {
        Long userId = Long.valueOf(principal.getSubject());
        List<WishListEntity> wishList = wishListRepository.findByUserId(userId);
        
        // Transformamos directamente a ProductDTO para el frontend
        List<ProductDTO> products = wishList.stream()
                .map(item -> productService.convertToDTO(item.getProduct())) 
                .toList();

        return ResponseEntity.ok(products);
    }
    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<WishListEntity> getById(@PathVariable("id") Long id){
        return wishListRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/product/{productId}")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteByProductId(@PathVariable Long productId, @AuthenticationPrincipal Jwt principal) {
        
        Long userId = Long.valueOf(principal.getSubject());

        WishListEntity wishListItem = wishListRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("El producto no se encuentra en tu lista de deseos"));

        wishListRepository.delete(wishListItem);

        return ResponseEntity.noContent().build();
    }

}
