package invoice.com.demo.features.stocks;

import invoice.com.demo.domain.Product;
import invoice.com.demo.domain.StockMovements;
import invoice.com.demo.domain.Stocks;
import invoice.com.demo.features.stocks.dto.MovementDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stocks , Long> {
    Optional<Stocks> findByProduct(Product product);
}
