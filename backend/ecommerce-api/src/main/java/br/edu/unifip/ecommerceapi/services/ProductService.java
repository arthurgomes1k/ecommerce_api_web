package br.edu.unifip.ecommerceapi.services;

import br.edu.unifip.ecommerceapi.models.Category;
import br.edu.unifip.ecommerceapi.models.Product;
import br.edu.unifip.ecommerceapi.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import java.lang.reflect.Field;

@Service
public class ProductService {

    final ProductRepository productRepository;
    final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService){
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public List<Product> findAll() {
        return productRepository.findByActiveTrue();
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findByActiveTrue(pageable);
    }

    public Optional<Product> findById(UUID id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product save(Product product, UUID categoryId){
        if (categoryId != null) {
            Category category = categoryService.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found."));
            product.setCategory(category);
        }
        return productRepository.save(product);
    }

    @Transactional
    public void hardDelete(Product product) {
        productRepository.delete(product);
    }

    @Transactional
    public void softDelete(Product product) {
        Optional<Product> productInstance = productRepository.findById(product.getId());
        productInstance.ifPresent(value -> value.setActive(false));
    }

    public Product partialUpdate(Product product, Map<Object, Object> objectMap) {
        if (objectMap.containsKey("category")) {
            String categoryId = (String) objectMap.get("category");
            Category category = categoryService.findById(UUID.fromString(categoryId))
                    .orElseThrow(() -> new RuntimeException("Category not found."));
            product.setCategory(category);
            objectMap.remove("category");
        }

        objectMap.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Product.class, (String) key);
            field.setAccessible(true);

            if (field.getType() == BigDecimal.class && value instanceof String) {
                value = new BigDecimal((String) value);
            }

            ReflectionUtils.setField(field, product, value);
        });
        return productRepository.save(product);
    }

    public Page<Product> findByActiveTrue(Pageable pageable) {
        return productRepository.findByActiveTrue(pageable);
    }

    public List<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    public List<Product> findByCategoryName(String name) {
        return productRepository.findByCategoryName(name);
    }
}
