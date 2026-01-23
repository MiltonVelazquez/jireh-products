package jireh.productos.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jireh.productos.models.SubcategoryEntity;

public interface SubcategoryRepository extends CrudRepository<SubcategoryEntity, Long>{

    List<SubcategoryEntity> findByNameContainingIgnoreCase(String name);

}
