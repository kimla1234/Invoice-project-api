package invoice.com.demo.features.invoice;


import invoice.com.demo.domain.Invoice;
import invoice.com.demo.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Page<Invoice> findAllByUser(User user, Pageable pageable);
    @Query("SELECT i FROM Invoice i LEFT JOIN FETCH i.items WHERE i.id = :id")
    Optional<Invoice> findByIdWithItems(@Param("id") Long id);
}
