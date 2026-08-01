package jireh.productos.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jireh.productos.models.WishListEntity;

public interface WishListRepository extends CrudRepository<WishListEntity, Long> {

    List<WishListEntity> findByUserId(Long userId);

    List<WishListEntity> findByUserIdAndProductId(Long userId, Long productId);

    boolean existsByUserIdAndProductId(Long userId, Long productId);
}
