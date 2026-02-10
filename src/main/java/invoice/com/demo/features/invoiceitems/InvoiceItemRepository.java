package invoice.com.demo.features.invoiceitems;

import invoice.com.demo.domain.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

    List<InvoiceItem> getInvoiceItemsByInvoiceId(Long id);
}
