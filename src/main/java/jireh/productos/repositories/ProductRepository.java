package jireh.productos.repositories;

import org.springframework.data.repository.CrudRepository;

import jireh.productos.models.ProductEntity;
import java.util.List;


public interface ProductRepository extends CrudRepository<ProductEntity, Long> {

    List<ProductEntity> findByNameContainingIgnoreCase(String name);

    // para productos relacionados
    List<ProductEntity> findTop6BySubcategoryIdAndIdNot(Long subcategoryId, Long excludeProductId);

    // para filtrar por categoria
    List<ProductEntity> findBySubcategoryId(Long subcategoryId);

}
