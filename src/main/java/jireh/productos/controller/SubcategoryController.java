package jireh.productos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jireh.productos.dto.SubcategoryDTO;
import jireh.productos.models.SubcategoryEntity;
import jireh.productos.services.SubcategoryService;


@RestController
@RequestMapping(path = "products/subcategory")
public class SubcategoryController {

    @Autowired
    private SubcategoryService subcategoryService;

    @GetMapping
    public ResponseEntity<List<SubcategoryDTO>> getAll() {
        return ResponseEntity.ok(subcategoryService.getSubcategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubcategoryDTO> getById(@PathVariable("id") Long id) {
        return subcategoryService.getSubcategory(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> saveUpdate(@RequestBody SubcategoryEntity subcategoryEntity) {
        subcategoryService.saveOrUpdate(subcategoryEntity);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        subcategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
