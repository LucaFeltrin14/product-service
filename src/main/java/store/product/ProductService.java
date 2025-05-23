package store.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product create(Product product) {
        return productRepository.save(new ProductModel(product)).to();
    }

    public Product findById(String id) {
        Optional<ProductModel> optional = productRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        return optional.get().to();
    }

    public void delete(String id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productRepository.deleteById(id);
    }

    public List<Product> findAll() {
    List<Product> produtos = new ArrayList<>();
    productRepository.findAll().forEach(model -> produtos.add(model.to()));
    return produtos;
    }
}
