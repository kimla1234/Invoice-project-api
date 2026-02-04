package invoice.com.demo.features.stocks;

import invoice.com.demo.domain.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StocksRepository extends JpaRepository<Stocks , Long> {
}
