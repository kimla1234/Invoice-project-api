package invoice.com.demo.features.invoice;


import invoice.com.demo.domain.Invoice;
import invoice.com.demo.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findAllByUser(User user);
}
