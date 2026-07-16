package jireh.productos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jireh.productos.dto.CategoryDTO;
import jireh.productos.models.CategoryEntity;
import jireh.productos.repositories.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    private CategoryDTO convertToDTO(CategoryEntity entity) {
        List<CategoryDTO.SubcategorySimpleDTO> subDTOs = entity.getSubcategories().stream()
            .map(sub -> new CategoryDTO.SubcategorySimpleDTO(sub.getId(), sub.getName()))
            .toList();
    
        return new CategoryDTO(entity.getId(), entity.getName(), subDTOs);
    }

    // obtener todas las categorías
    public List<CategoryDTO> getCategories() {
        List<CategoryEntity> categories = (List<CategoryEntity>) categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToDTO)
                .toList();
    }

    // obtener una categoría por id
    public Optional<CategoryDTO> getCategory(Long id) {
        return categoryRepository.findById(id).map(this::convertToDTO);
    }

    // guardar o actualizar
    public void saveOrUpdate(CategoryEntity category) {
        categoryRepository.save(category);
    }

    // eliminar
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<CategoryDTO> searchCategories(String name) {
        List<CategoryEntity> results = categoryRepository.findByNameContainingIgnoreCase(name);
        return results.stream()
                .map(this::convertToDTO)
                .toList();
    }
}
