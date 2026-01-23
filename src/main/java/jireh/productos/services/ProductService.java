package jireh.productos.services;

import java.util.List;
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

    // sumar visita
    public Optional<ProductEntity> getProductAndIncrementView(Long id) {
        Optional<ProductEntity> productOpt = productRepository.findById(id);
        productOpt.ifPresent(p -> {
            p.setViews(p.getViews() + 1);
            productRepository.save(p);
        });
        return productOpt;
    }

    //obtener los mas visitados
    public List<ProductEntity> getTopVisited() {
        List<ProductEntity> top = productRepository.findTop8ByOrderByViewsDesc();
        // Lógica de relleno si no hay 8:
        if (top.size() < 8) {
            int needed = 8 - top.size();
            List<ProductEntity> randoms = productRepository.findRandomProducts(needed);
            top.addAll(randoms);
        }
        return top;
    }
}
