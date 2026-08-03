package jireh.productos.dto;

import java.time.LocalDateTime;

public record ReviewDTO (

    private Long id,
    private Long userId,
    private String userName,
    private Long productId,
    private Integer rating,
    private String comment,
    private LocalDateTime createdAt,
){}
