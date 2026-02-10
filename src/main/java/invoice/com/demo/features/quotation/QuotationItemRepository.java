package invoice.com.demo.features.quotation;

import invoice.com.demo.domain.QuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationItemRepository extends JpaRepository<QuotationItem, Long> {

}
