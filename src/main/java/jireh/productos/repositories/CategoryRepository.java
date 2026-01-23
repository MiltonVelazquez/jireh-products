package jireh.productos.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jireh.productos.models.CategoryEntity;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long>{

    List<CategoryEntity> findByNameContainingIgnoreCase(String name);


}
