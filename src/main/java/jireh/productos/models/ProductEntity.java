package jireh.productos.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.GenerationType;

import lombok.Data;

@Data
@Entity
@Table(name = "producto")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nombre")
    private String name;

    @NotBlank
    @Column(precision = 10, scale = 2, name = "precio")
    private BigDecimal price;

    @NotBlank
    @Column(name = "descripcion")
    private String description;

    @NotBlank
    @Column(name = "url_descarga")
    private String urlDownload;

    @Column(name = "stock")
    @Min(0)
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubcategoryEntity subcategory;

    @Column(name = "views")
    private Long views = 0L;
}
