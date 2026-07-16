package jireh.productos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jireh.productos.dto.SubcategoryDTO;
import jireh.productos.models.SubcategoryEntity;
import jireh.productos.repositories.SubcategoryRepository;

@Service
public class SubcategoryService {

    @Autowired
    SubcategoryRepository subcategoryRepository;

    private SubcategoryDTO convertToDTO(SubcategoryEntity entity) {
        return new SubcategoryDTO(
            entity.getId(),
            entity.getName(),
            entity.getCategory().getId(),
            entity.getCategory().getName()
        );
    }

    public List<SubcategoryDTO> getSubcategories() {
        List<SubcategoryEntity> subcategories = (List<SubcategoryEntity>) subcategoryRepository.findAll();
        return subcategories.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public Optional<SubcategoryDTO> getSubcategory(Long id) {
        return subcategoryRepository.findById(id).map(this::convertToDTO);
    }

    public void saveOrUpdate(SubcategoryEntity subcategory) {
        subcategoryRepository.save(subcategory);
    }

    public void delete(Long id) {
        subcategoryRepository.deleteById(id);
    }

}
