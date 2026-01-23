package jireh.productos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jireh.productos.models.CategoryEntity;
import jireh.productos.repositories.CategoryRepository;
import jireh.productos.services.CategoryService;


@RestController
@RequestMapping(path = "products/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository; 

    @GetMapping
    public Iterable<CategoryEntity> getAll(){
        return categoryService.getCategories();
    }

    @GetMapping("/{id}")
    public Optional<CategoryEntity> getById(@PathVariable("id") Long id){
        return categoryService.getCategory(id);
    }

    @PostMapping
    public void saveUpdate(@RequestBody CategoryEntity categoryEntity){
        categoryService.saveOrUpdate(categoryEntity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        categoryService.delete(id);
    }

    @GetMapping("/search/{input}")
    public ResponseEntity<List<CategoryEntity>> search(@PathVariable("input") String input){
        List<CategoryEntity> results = categoryRepository.findByNameContainingIgnoreCase(input);
        return ResponseEntity.ok(results);
    }

}
