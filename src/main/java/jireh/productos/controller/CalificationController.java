package jireh.productos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import jireh.productos.models.CalificationEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import jireh.productos.repositories.CalificationRepository;

@RestController
@RequestMapping(path = "products/calification")
public class CalificationController {


    @Autowired
    private CalificationRepository calificationRepository;

    @PostMapping
    public ResponseEntity<CalificationEntity> save(@RequestBody CalificationEntity calificationEntity, @AuthenticationPrincipal Jwt principal){
        
        Long userId = Long.valueOf(principal.getSubject());

        calificationEntity.setUserId(userId);

        CalificationEntity saved = calificationRepository.save(calificationEntity);

        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/deleteCalification/{id}")
    public void delete(@PathVariable("id") Long id){
        calificationRepository.deleteById(id);
    }

    @GetMapping
    public Iterable<CalificationEntity> getAll(){
        return calificationRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<CalificationEntity> getById(@PathVariable("id") Long id){
        return calificationRepository.findById(id);
    }

    // @GetMapping("/search/{input}")
    // public ResponseEntity<List<CalificationEntity>> search(@PathVariable("input") String input){
    //     List<CalificationEntity> results = calificationRepository.findByNameContainingIgnoreCase(input);
    //     return ResponseEntity.ok(results);
    // }
}

