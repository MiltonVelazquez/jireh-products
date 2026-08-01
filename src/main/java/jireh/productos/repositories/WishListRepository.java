package jireh.productos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import jireh.productos.models.WishListEntity;

public interface WishListRepository extends CrudRepository<WishListEntity, Long>{

    List<WishListEntity> findByUserId(Long userId);

    Optional<WishListEntity> findByUserIdAndProductId(Long userId, Long productId);
    
    List<WishListEntity> findByUserIdAndProductId(Long userId, Long productId);

    boolean existsByUserIdAndProductId(Long userId, Long productId);
}
