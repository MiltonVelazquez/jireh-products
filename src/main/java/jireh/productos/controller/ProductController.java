package jireh.productos.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jireh.productos.models.ProductEntity;
import jireh.productos.repositories.ProductRepository;
import jireh.productos.services.ProductService;
import java.util.List;
import java.lang.Iterable;


@RestController
@RequestMapping(path = "products/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<Iterable<ProductEntity>> getProducts(
        @RequestParam(name = "subcategoriaId", required = false) Long subcategoryId) {
    
        if (subcategoryId != null) {
            return ResponseEntity.ok(productService.getProductsBySubcategory(subcategoryId));
        }
        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> getById(@PathVariable("id") Long id){
        return productService.getProductAndIncrementView(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public void saveUpdate(@RequestBody ProductEntity productEntity){
        productService.saveOrUpdate(productEntity);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        productService.delete(id);
    }

    // buscar
    @GetMapping("/search/{input}")
    public ResponseEntity<List<ProductEntity>> search(@PathVariable("input") String input){
        List<ProductEntity> results = productRepository.findByNameContainingIgnoreCase(input);
        return ResponseEntity.ok(results);
    }

    // relacionados
    @GetMapping("/related/{id}")
    public ResponseEntity<List<ProductEntity>> getRelatedProducts(@PathVariable Long id) {
        
        ProductEntity currentProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        
        Long subcategoryId = currentProduct.getSubcategory().getId();

        List<ProductEntity> results = productRepository.findTop6BySubcategoryIdAndIdNot(subcategoryId, id);

        return ResponseEntity.ok(results);
    }
    
    // obtoner los 8 mas recientes
    @GetMapping("/latest")
    public ResponseEntity<List<ProductEntity>> getLatest() {
        return ResponseEntity.ok(productRepository.findTop8ByOrderByIdDesc());
    }

    // obtener los 8 mas recientes o al azar
    @GetMapping("/trending")
    public ResponseEntity<List<ProductEntity>> getTrending() {
        return ResponseEntity.ok(productService.getTopVisited());
    }

}
