package jireh.productos.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jireh.productos.models.SubcategoryEntity;
import jireh.productos.repositories.SubcategoryRepository;

@Service
public class SubcategoryService {

    @Autowired
    SubcategoryRepository subcategoryRepository;

    // Read categories
    public Iterable<SubcategoryEntity> getSubcategories(){
        return subcategoryRepository.findAll();
    }

    // Read a category
    public Optional<SubcategoryEntity> getSubcategory(Long id){
        return subcategoryRepository.findById(id);
    }

    // Create or update 
    public void saveOrUpdate(SubcategoryEntity subcategory){
        subcategoryRepository.save(subcategory);
    }

    // Delete
    public void delete(Long id){
        subcategoryRepository.deleteById(id);
    }

}
