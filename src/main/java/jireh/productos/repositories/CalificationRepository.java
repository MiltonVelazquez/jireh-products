package jireh.productos.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import jireh.productos.models.CalificationEntity;

public interface CalificationRepository extends CrudRepository<CalificationEntity, Long>{

    List<CalificationEntity> findByNameContainingIgnoreCase(String description);


}
