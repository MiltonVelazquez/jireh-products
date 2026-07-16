package jireh.productos.dto;

public record SubcategoryDTO (
    Long id,
    String name,
    Long categoryId,
    String categoryName
) {}