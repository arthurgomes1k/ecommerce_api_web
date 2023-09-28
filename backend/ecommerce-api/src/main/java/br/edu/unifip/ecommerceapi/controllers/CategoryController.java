package br.edu.unifip.ecommerceapi.controllers;

import br.edu.unifip.ecommerceapi.dtos.CategoryDto;
import br.edu.unifip.ecommerceapi.models.Category;
import br.edu.unifip.ecommerceapi.models.Product;
import br.edu.unifip.ecommerceapi.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/categories")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoryController {
    final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategorys() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findByActiveTrue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable(value = "id") UUID id) {
        Optional<Category> categoryOptional = categoryService.findById(id);

        // Verificar se o registro está ativo
        if (categoryOptional.isPresent() && !categoryOptional.get().isActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category is not active.");
        }

        return categoryOptional.<ResponseEntity<Object>>map(category -> ResponseEntity.status(HttpStatus.OK).body(category)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found."));
    }

    @PostMapping
    public ResponseEntity<Object> saveCategory(@RequestBody @Valid CategoryDto categoryDto) {
        var category = new Category();
        BeanUtils.copyProperties(categoryDto, category);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> softDeleteProduct(@PathVariable(value = "id") UUID id) {
        Optional<Category> categoryOptional = categoryService.findById(id);
        if (categoryOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found.");
        }

        Category instance = categoryOptional.get();

        // Verificar se o registro está ativo
        if (!instance.isActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category is not active.");
        }

        categoryService.softDelete(instance);
        return ResponseEntity.status(HttpStatus.OK).body("Category deleted successfully.");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable(value = "id") UUID id, @RequestBody Map<Object, Object> objectMap) {
        Optional<Category> categoryOptional = categoryService.findById(id);
        if (categoryOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found.");
        }

        Category instance = categoryOptional.get();

        // Verificar se o registro está ativo
        if (!instance.isActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category is not active.");
        }

        categoryService.partialUpdate(instance, objectMap);
        return ResponseEntity.status(HttpStatus.OK).body(categoryOptional.get());
    }
}
