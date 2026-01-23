package jireh.productos.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jireh.productos.models.WishListEntity;

public interface WishListRepository extends CrudRepository<WishListEntity, Long>{

    List<WishListEntity> findByUserId(Long userId);

}
