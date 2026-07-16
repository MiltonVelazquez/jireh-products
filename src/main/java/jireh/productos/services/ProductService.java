package jireh.productos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jireh.productos.dto.ProductDTO;
import jireh.productos.models.ProductEntity;
import jireh.productos.repositories.ProductRepository;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public ProductDTO convertToDTO(ProductEntity product) {
        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getDescription(),
            product.getImageUrl(),
            product.getViews(),
            product.getSubcategory().getName() 
        );
    }

    //obtener todos
    public List<ProductDTO> getProductsDTO() {
        // Antes: productRepository.findAll()
        List<ProductEntity> products = productRepository.findByActiveTrue(); 
        return products.stream()
                       .map(this::convertToDTO)
                       .toList();
    }

    // obtener uno solo
    public Optional<ProductDTO> getProductDTO(Long id) {
        return productRepository.findById(id).map(this::convertToDTO);
    }

    // Incrementar vista
    public Optional<ProductDTO> getProductAndIncrementView(Long id) {
        Optional<ProductEntity> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            ProductEntity p = productOpt.get();
            p.setViews(p.getViews() + 1);
            productRepository.save(p);
            return Optional.of(convertToDTO(p));
        }
        return Optional.empty();
    }
    // crear o actualizar
    public void saveOrUpdate(ProductEntity product){
        productRepository.save(product);
    }

    // borrar por id
    public void delete(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El producto con ID " + id + " no existe."));
        
        product.setActive(false);
        productRepository.save(product);
    }

    // filtrar por categoria
    public List<ProductEntity> getProductsBySubcategory(Long subcategoryId) {
        return productRepository.findBySubcategoryIdAndActiveTrue(subcategoryId);
    }


    public List<ProductDTO> searchProducts(String input) {
        return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(input)
                .stream().map(this::convertToDTO).toList();
    }

    public List<ProductDTO> getLatestProducts() {
        return productRepository.findTop8ByActiveTrueOrderByCreatedAtDesc()
            .stream()
            .map(this::convertToDTO)
            .toList();
    }

    public List<ProductDTO> getRelatedProducts(Long id) {
        ProductEntity current = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        return productRepository.findTop6BySubcategoryIdAndIdNotAndActiveTrue(current.getSubcategory().getId(), id)
                .stream().map(this::convertToDTO).toList();
    }

    public List<ProductDTO> getTrendingProducts() {
        List<ProductEntity> top = productRepository.findTop8ByActiveTrueOrderByViewsDesc();
        
        if (top.size() < 8) {
            int needed = 8 - top.size();
            List<Long> existingIds = top.stream().map(ProductEntity::getId).toList();
            
            List<ProductEntity> randomProducts = productRepository.findRandomProducts(needed).stream()
                    .filter(p -> !existingIds.contains(p.getId())) // Evita duplicados
                    .toList();
            
            top.addAll(randomProducts);
        }
        
        return top.stream().map(this::convertToDTO).toList();
    }
}
