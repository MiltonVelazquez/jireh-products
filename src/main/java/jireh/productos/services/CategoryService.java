package jireh.productos.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jireh.productos.models.CategoryEntity;
import jireh.productos.repositories.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    // Read categories
    public Iterable<CategoryEntity> getCategories(){
        return categoryRepository.findAll();
    }

    // Read a category
    public Optional<CategoryEntity> getCategory(Long id){
        return categoryRepository.findById(id);
    }

    // Create or update 
    public void saveOrUpdate(CategoryEntity category){
        categoryRepository.save(category);
    }

    // Delete
    public void delete(Long id){
        categoryRepository.deleteById(id);
    }
}
