package jireh.productos.controller;

import jireh.productos.dto.ProductDTO;
import jireh.productos.models.WishListEntity;
import jireh.productos.repositories.WishListRepository;
import jireh.productos.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "products/wishlist")
public class WishListController {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> save(@RequestBody WishListEntity wishListEntity) {
        WishListEntity saved = wishListRepository.save(wishListEntity);
        return ResponseEntity.status(201).body(productService.convertToDTO(saved.getProduct()));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getUserWishList(@RequestParam Long userId) {
        List<WishListEntity> wishList = wishListRepository.findByUserId(userId);
        
        List<ProductDTO> products = wishList.stream()
                .map(item -> productService.convertToDTO(item.getProduct())) 
                .toList();

        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> deleteByProductId(@PathVariable Long productId, @RequestParam Long userId) {
        WishListEntity wishListItem = wishListRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("El producto no se encuentra en tu lista de deseos"));

        wishListRepository.delete(wishListItem);

        return ResponseEntity.noContent().build();
    }
}
