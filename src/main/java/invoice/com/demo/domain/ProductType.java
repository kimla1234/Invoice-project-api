package invoice.com.demo.domain;

import invoice.com.demo.audit.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="products_type")
public class ProductType extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Boolean status = true;

    // One type → many products
    @OneToMany(mappedBy = "productType", fetch = FetchType.LAZY)
    private List<Product> products;
}
