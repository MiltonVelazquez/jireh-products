package jireh.productos.dto;

import java.time.LocalDateTime;

public record ReviewDTO (

    Long id,
    Long userId,
    String userName,
    Long productId,
    Integer rating,
    String comment,
    LocalDateTime createdAt,
){}
