package jireh.productos.dto;

import java.math.BigDecimal;

public record ProductDTO(
    Long id,
    String name,
    BigDecimal price,
    String description,
    String imageUrl,
    Long views,
    String subcategoryName 
) {}