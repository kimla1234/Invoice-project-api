package invoice.com.demo.features.stocks;

import invoice.com.demo.domain.StockMovements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockMovementRepository  extends JpaRepository<StockMovements , Long> {
}
