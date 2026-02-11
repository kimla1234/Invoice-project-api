package invoice.com.demo.features.products;

import invoice.com.demo.domain.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
    List<ProductType> findByUserEmail(String email);
    boolean existsByNameIgnoreCase(String name);
}
