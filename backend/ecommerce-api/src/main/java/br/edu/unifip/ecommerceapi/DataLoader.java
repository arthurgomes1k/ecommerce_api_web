package br.edu.unifip.ecommerceapi;

import br.edu.unifip.ecommerceapi.models.Product;
import br.edu.unifip.ecommerceapi.services.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component // Anote a classe como um componente Spring
public class DataLoader implements CommandLineRunner {
    final ProductService productService;

    public DataLoader(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verifique se já existem dados no banco de dados
        if (productService.findAll().isEmpty()) {
            // Inicializar dados no banco de dados apenas se não houver dados existentes

            Product product1 = new Product("Produto 1", "Descrição do Produto 1", new BigDecimal("10.99"));
            productService.save(product1, null);

            Product product2 = new Product("Produto 2", "Descrição do Produto 2", new BigDecimal("19.99"));
            productService.save(product2, null);
        }
    }
}
