package jireh.productos.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jireh.productos.dto.ProductDTO;
import jireh.productos.models.ProductEntity;
import jireh.productos.repositories.ProductRepository;

@Service
@Transactional(readOnly = true)
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductDTO convertToDTO(ProductEntity product) {
        if (product == null) {
            return null;
        }

        Long subcategoryId = null;
        String subcategoryName = null;
        Long categoryId = null;
        String categoryName = null;

        if (product.getSubcategory() != null) {
            subcategoryId = product.getSubcategory().getId();
            subcategoryName = product.getSubcategory().getName();

            if (product.getSubcategory().getCategory() != null) {
                categoryId = product.getSubcategory().getCategory().getId();
                categoryName = product.getSubcategory().getCategory().getName();
            }
        }

        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getDescription(),
            product.getImageUrl(),
            product.getViews(),
            categoryId,
            categoryName,
            subcategoryId,
            subcategoryName
        );
    }

    // obtener todos
    public List<ProductDTO> getProductsDTO() {
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
    @Transactional
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
    @Transactional 
    public void saveOrUpdate(ProductEntity product){
        productRepository.save(product);
    }

    // borrar por id
    @Transactional 
    public void delete(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El producto con ID " + id + " no existe."));
        
        product.setActive(false);
        productRepository.save(product);
    }

    public List<ProductDTO> getProductsBySubcategoryDTO(Long subcategoryId) {
        return productRepository.findBySubcategoryIdAndActiveTrue(subcategoryId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ProductDTO> getProductsByCategoryDTO(Long categoryId) {
        return productRepository.findBySubcategoryCategoryIdAndActiveTrue(categoryId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ProductEntity> getProductsBySubcategory(Long subcategoryId) {
        return productRepository.findBySubcategoryIdAndActiveTrue(subcategoryId);
    }

    public List<ProductEntity> getProductsByCategory(Long categoryId) {
        return productRepository.findBySubcategoryCategoryIdAndActiveTrue(categoryId);
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
        
        if (current.getSubcategory() == null) {
            return Collections.emptyList();
        }

        return productRepository.findTop6BySubcategoryIdAndIdNotAndActiveTrue(current.getSubcategory().getId(), id)
                .stream().map(this::convertToDTO).toList();
    }

    public List<ProductDTO> getTrendingProducts() {
        List<ProductEntity> top = productRepository.findTop8ByActiveTrueOrderByViewsDesc();
        
        if (top.size() < 8) {
            int needed = 8 - top.size();
            List<Long> existingIds = top.stream().map(ProductEntity::getId).toList();
            
            List<ProductEntity> randomProducts = productRepository.findRandomProducts(needed).stream()
                    .filter(p -> !existingIds.contains(p.getId()))
                    .toList();
            
            top.addAll(randomProducts);
        }
        
        return top.stream().map(this::convertToDTO).toList();
    }
}
