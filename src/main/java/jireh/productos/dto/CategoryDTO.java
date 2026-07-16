package jireh.productos.dto;

import java.util.List;

public record CategoryDTO(
    Long id,
    String name,
    List<SubcategorySimpleDTO> subcategories // <-- Cambiado a DTO Simple
) {
    // Record auxiliar compacto para evitar redundancia de datos del padre
    public record SubcategorySimpleDTO(
        Long id,
        String name
    ) {}
}