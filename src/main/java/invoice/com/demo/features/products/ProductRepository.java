package invoice.com.demo.features.products;

import invoice.com.demo.domain.Product;
import invoice.com.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product , String> {
    List<Product> findByUserAndIsDeletedFalse(User user);
    Optional<Product> findByUuid(String uuid);
}
