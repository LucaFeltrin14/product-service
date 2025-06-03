package store.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /** ----------------------------------------------------------------
     *  CREATE
     *  - Atualiza o cache individual (products::<id>)
     *  - Invalida a lista completa (productsAll) para forçar refresh
     *  ---------------------------------------------------------------*/
    @Caching(
        put   = { @CachePut(value = "products", key = "#result.id") },
        evict = { @CacheEvict(value = "productsAll", allEntries = true) }
    )
    public Product create(Product product) {
        return productRepository.save(new ProductModel(product)).to();
    }

    /** ----------------------------------------------------------------
     *  READ BY ID
     *  - Consulta primeiro no cache individual
     *  ---------------------------------------------------------------*/
    @Cacheable(value = "products", key = "#id")
    public Product findById(String id) {
        Optional<ProductModel> optional = productRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        return optional.get().to();
    }

    /** ----------------------------------------------------------------
     *  DELETE
     *  - Remove do banco
     *  - Evicta o cache individual e a lista
     *  ---------------------------------------------------------------*/
    @Caching(
        evict = {
            @CacheEvict(value = "products", key = "#id"),
            @CacheEvict(value = "productsAll", allEntries = true)
        }
    )
    public void delete(String id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productRepository.deleteById(id);
    }

    /** ----------------------------------------------------------------
     *  READ ALL
     *  - A lista é cacheada; qualquer create/delete limpa este cache,
     *    então o próximo findAll() reconstrói a lista atualizada.
     *  ---------------------------------------------------------------*/
    @Cacheable("productsAll")
    public List<Product> findAll() {
        List<Product> produtos = new ArrayList<>();
        productRepository.findAll().forEach(model -> produtos.add(model.to()));
        return produtos;
    }
}
