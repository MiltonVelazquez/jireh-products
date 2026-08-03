package jireh.productos.dto;

import java.math.BigDecimal;

public record ProductDTO(
    Long id,
    String name,
    BigDecimal price,
    String description,
    String imageUrl,
    Long views,
    Long categoryId,       
    String categoryName,   
    Long subcategoryId,    
    String subcategoryName 
) {}
