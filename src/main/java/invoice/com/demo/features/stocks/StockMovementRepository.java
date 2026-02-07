package invoice.com.demo.features.stocks;

import invoice.com.demo.domain.Product;
import invoice.com.demo.domain.StockMovements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface StockMovementRepository  extends JpaRepository<StockMovements , Long> {
    List<StockMovements> findByProduct(Product product);
}
