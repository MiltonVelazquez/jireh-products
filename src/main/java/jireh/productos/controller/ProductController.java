package jireh.productos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jireh.productos.dto.ProductDTO;
import jireh.productos.models.ProductEntity;
import jireh.productos.repositories.ProductRepository;
import jireh.productos.services.ProductService;
import java.util.List;


@RestController
@RequestMapping(path = "products/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> saveUpdate(
        @RequestPart("product") ProductEntity productEntity,
        @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(imageFile);
                productEntity.setImageUrl(imageUrl);
            }
        
            productService.saveOrUpdate(productEntity);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> update(
                            @PathVariable Long id,
                            @RequestPart("product") ProductEntity productEntity,
                            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        
        return productService.getProductDTO(id).map(existing -> {
            try {
                if (imageFile != null && !imageFile.isEmpty()) {
                    String imageUrl = cloudinaryService.uploadImage(imageFile);
                    productEntity.setImageUrl(imageUrl);
                } else {
                    productEntity.setImageUrl(existing.getImageUrl());
                }

                productEntity.setId(id);
                productService.saveOrUpdate(productEntity);
                return ResponseEntity.ok().<Void>build();
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build();
            }
        }).orElse(ResponseEntity.notFound().build());
        }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> delete(@PathVariable("id") @NonNull Long id) {
    return productRepository.findById(id).map(product -> {
        product.setActive(false);
        productRepository.save(product);
        return ResponseEntity.noContent().build();
    }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts() {
        return ResponseEntity.ok(productService.getProductsDTO());
    }

    // GET by ID: 200 OK o 404 Not Found
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable("id") Long id) {
        return productService.getProductAndIncrementView(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search/{input}")
    public ResponseEntity<List<ProductDTO>> search(@PathVariable("input") String input) {
        return ResponseEntity.ok(productService.searchProducts(input));
    }

    @GetMapping("/related/{id}")
    public ResponseEntity<List<ProductDTO>> getRelated(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getRelatedProducts(id));
    }

    @GetMapping("/latest")
    public ResponseEntity<List<ProductDTO>> getLatest() {
        return ResponseEntity.ok(productService.getLatestProducts());
    }

    @GetMapping("/trending")
    public ResponseEntity<List<ProductDTO>> getTrending() {
        return ResponseEntity.ok(productService.getTrendingProducts());
    }

}
