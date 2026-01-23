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

import jireh.productos.models.SubcategoryEntity;
import jireh.productos.repositories.SubcategoryRepository;
import jireh.productos.services.SubcategoryService;
import java.lang.Iterable;


@RestController
@RequestMapping(path = "products/subcategory")
public class SubcategoryController {

    @Autowired
    private SubcategoryService subcategoryService;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @GetMapping
    public Iterable<SubcategoryEntity> getAll(){
        return subcategoryService.getSubcategories();
    }

    @GetMapping("/{id}")
    public Optional<SubcategoryEntity> getById(@PathVariable("id") Long id){
        return subcategoryService.getSubcategory(id);
    }

    @PostMapping
    public void saveUpdate(@RequestBody SubcategoryEntity categoryEntity){
        subcategoryService.saveOrUpdate(categoryEntity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        subcategoryService.delete(id);
    }

    @GetMapping("/search/{input}")
    public ResponseEntity<List<SubcategoryEntity>> search(@PathVariable("input") String input){
        List<SubcategoryEntity> results = subcategoryRepository.findByNameContainingIgnoreCase(input);
        return ResponseEntity.ok(results);
    }
}
