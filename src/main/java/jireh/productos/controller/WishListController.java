package jireh.productos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jireh.productos.models.ProductEntity;
import jireh.productos.models.WishListEntity;
import jireh.productos.repositories.WishListRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping(path = "products/wishlist")
public class WishListController {

    @Autowired
    private WishListRepository wishListRepository;

    @PostMapping
    public ResponseEntity<WishListEntity> save(@RequestBody WishListEntity wishListEntity, @AuthenticationPrincipal Jwt principal){

        Long userId = Long.valueOf(principal.getSubject());

        wishListEntity.setUserId(userId);

        WishListEntity saved = wishListRepository.save(wishListEntity);

        return ResponseEntity.ok(saved);

    }

    @DeleteMapping("/deleteWishList/{id}")
    public void delete(@PathVariable("id") Long id){
        wishListRepository.deleteById(id);
    }

    @GetMapping
    public ResponseEntity<List<ProductEntity>> getUserWishList(@AuthenticationPrincipal Jwt principal) {

        Long userId = Long.valueOf(principal.getSubject());

    
        List<WishListEntity> wishList = wishListRepository.findByUserId(userId);

        List<ProductEntity> products = wishList.stream()
            .map(WishListEntity::getProduct)
            .toList();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public Optional<WishListEntity> getById(@PathVariable("id") Long id){
        return wishListRepository.findById(id);
    }

}
