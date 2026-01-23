package jireh.productos.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jireh.productos.models.ProductEntity;
import jireh.productos.repositories.ProductRepository;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    // todos los productos
    public Iterable<ProductEntity> getProducts(){
        return productRepository.findAll();
    }

    // un producto
    public Optional<ProductEntity> getProduct(Long id){
        return productRepository.findById(id);
    }

    // crear o actualizar
    public void saveOrUpdate(ProductEntity product){
        productRepository.save(product);
    }

    // borrar por id
    public void delete(Long id){
        productRepository.deleteById(id);;
    }

    // filtrar por categoria
    public List<ProductEntity> getProductsBySubcategory(Long subcategoryId) {
        return productRepository.findBySubcategoryId(subcategoryId);
    }
}
