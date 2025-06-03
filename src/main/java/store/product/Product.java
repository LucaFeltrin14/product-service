package store.product;

import java.io.Serializable;           // ⬅ novo import
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Builder
@Data @Accessors(fluent = true)
public class Product implements Serializable {   // ⬅ implementa Serializable
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private double price;
    private String unit;
}
