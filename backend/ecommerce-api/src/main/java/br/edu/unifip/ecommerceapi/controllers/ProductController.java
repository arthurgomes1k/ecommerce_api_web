package br.edu.unifip.ecommerceapi.controllers;

import br.edu.unifip.ecommerceapi.dtos.ProductDto;
import br.edu.unifip.ecommerceapi.models.Product;
import br.edu.unifip.ecommerceapi.services.ProductService;
import br.edu.unifip.ecommerceapi.utils.FileUploadUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("api/products")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {
    final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable) {
        Page<Product> products = productService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable(value = "id") UUID id) {
        Optional<Product> productOptional = productService.findById(id);

        // Verificar se o registro está ativo
        if (productOptional.isPresent() && !productOptional.get().isActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product is not active.");
        }

        return productOptional.<ResponseEntity<Object>>map(product -> ResponseEntity.status(HttpStatus.OK).body(product)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found."));
    }

    @PostMapping
    public ResponseEntity<Object> saveProduct(@Valid ProductDto productDto, HttpServletRequest request) throws IOException {
        var product = new Product();

        BeanUtils.copyProperties(productDto, product); // O que vai ser convertido para o quê vai ser convertido

        UUID categoryId = null;

        if (productDto.category() != null) {
            categoryId = productDto.category();
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile("image");

        if (multipartFile != null) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            String uploadDir = "product-images/";

            try {
                String filecode = FileUploadUtil.saveFile(fileName, uploadDir, multipartFile);
                product.setImage("/api/images/product-images/" + filecode);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Image not accepted.");
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product, categoryId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> softDeleteProduct(@PathVariable(value = "id") UUID id) {
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }

        Product instance = productOptional.get();

        // Verificar se o registro está ativo
        if (!instance.isActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product is not active.");
        }

        productService.softDelete(instance);
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id, HttpServletRequest request) {
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }

        Product instance = productOptional.get();

        // Verificar se o registro está ativo
        if (!instance.isActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product is not active.");
        }

        Map<Object, Object> objectMap = new HashMap<>();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            objectMap.put(entry.getKey(), entry.getValue()[0]);
        }

        // Salvar a url da imagem em uma variável separada
        String imageUrl = null;
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile("image");
        if (multipartFile != null) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            String uploadDir = "product-images/";

            try {
                String filecode = FileUploadUtil.saveFile(fileName, uploadDir, multipartFile);
                imageUrl = "/api/images/product-images/" + filecode;
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Image not accepted.");
            }
        }

        // Adicionar a url da imagem ao objeto mapeado, se ela foi enviada
        if (imageUrl != null) {
            objectMap.put("image", imageUrl);
        }

        productService.partialUpdate(instance, objectMap);

        return ResponseEntity.status(HttpStatus.OK).body(instance);
    }

    @GetMapping("/findByName")
    public ResponseEntity<List<Product>> getProductByName(@Validated @RequestParam(value = "name") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findByName(name));
    }

    @GetMapping("/findByCategoryName")
    public ResponseEntity<List<Product>> getProductByCategoryName(@Validated @RequestParam(value = "name") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findByCategoryName(name));
    }
}
