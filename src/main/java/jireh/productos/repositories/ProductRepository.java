package jireh.productos.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import jireh.productos.models.ProductEntity;
import java.util.List;


public interface ProductRepository extends CrudRepository<ProductEntity, Long> {

    List<ProductEntity> findByNameContainingIgnoreCaseAndActiveTrue(String name);

    // para productos relacionados
    List<ProductEntity> findTop6BySubcategoryIdAndIdNotAndActiveTrue(Long subcategoryId, Long excludeProductId);

    // para filtrar por categoria
    List<ProductEntity> findBySubcategoryIdAndActiveTrue(Long subcategoryId);


    // encontrar ultimos 8 agregados
    List<ProductEntity> findTop8ByActiveTrueOrderByIdDesc();
    
    // encontrar los 8 mas visitados
    List<ProductEntity> findTop8ByActiveTrueOrderByViewsDesc();
    
    List<ProductEntity> findTop8ByActiveTrueOrderByCreatedAtDesc();

    List<ProductEntity> findByActiveTrue(); 

    // volver las vistas a 0
    @Transactional
    @Modifying
    @Query("UPDATE ProductEntity p SET p.views = 0")
    void resetAllViews();
    
    @Query(value = "SELECT * FROM producto WHERE activo = true ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<ProductEntity> findRandomProducts(int limit);
}
