package jireh.productos.controller;

import jireh.productos.dto.ProductDTO;
import jireh.productos.models.WishListEntity;
import jireh.productos.repositories.WishListRepository;
import jireh.productos.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/wishlist")
public class WishListController {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> save(@RequestBody WishListEntity wishListEntity) {
        Long userId = wishListEntity.getUser().getId();
        Long productId = wishListEntity.getProduct().getId();

        if (wishListRepository.existsByUserIdAndProductId(userId, productId)) {
            return ResponseEntity.ok(productService.convertToDTO(wishListEntity.getProduct()));
        }

        WishListEntity saved = wishListRepository.save(wishListEntity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.convertToDTO(saved.getProduct()));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getUserWishList(@RequestParam Long userId) {
        List<WishListEntity> wishList = wishListRepository.findByUserId(userId);

        List<ProductDTO> products = wishList.stream()
                .map(item -> productService.convertToDTO(item.getProduct()))
                .toList();

        return ResponseEntity.ok(products);
    }

    @Transactional 
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> deleteByProductId(@PathVariable Long productId, @RequestParam Long userId) {
        List<WishListEntity> wishListItems = wishListRepository.findByUserIdAndProductId(userId, productId);

        if (wishListItems.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        wishListRepository.deleteAll(wishListItems);

        return ResponseEntity.noContent().build();
    }
}
