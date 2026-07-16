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

import jireh.productos.dto.CategoryDTO;
import jireh.productos.models.CategoryEntity;
import jireh.productos.services.CategoryService;


@RestController
@RequestMapping(path = "products/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Obtener todas
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAll() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    // obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable("id") Long id) {
        return categoryService.getCategory(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Guardar
    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> saveUpdate(@RequestBody CategoryEntity categoryEntity) {
        categoryService.saveOrUpdate(categoryEntity);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // En CategoryController.java
    @GetMapping("/search/{input}")
    public ResponseEntity<List<CategoryDTO>> search(@PathVariable("input") String input){
    // Crea este método en CategoryService que devuelva List<CategoryDTO>
        return ResponseEntity.ok(categoryService.searchCategories(input));
    }

}
