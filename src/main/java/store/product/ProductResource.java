package store.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductResource implements ProductController {

    @Autowired
    private ProductService service;

    @Override
    public ResponseEntity<ProductOut> create(ProductIn in) {
        Product product = service.create(ProductParser.fromIn(in));
        return ResponseEntity.status(201).body(ProductParser.fromEntity(product));
    }

    @Override
    public ResponseEntity<List<ProductOut>> findAll() {
        List<Product> produtos = service.findAll();
        return ResponseEntity.ok(
            produtos.stream()
                    .map(ProductParser::fromEntity)
                    .toList()
        );
    }

    @Override
    public ResponseEntity<ProductOut> findById(String id) {
        return ResponseEntity.ok(
            ProductParser.fromEntity(service.findById(id))
        );
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
