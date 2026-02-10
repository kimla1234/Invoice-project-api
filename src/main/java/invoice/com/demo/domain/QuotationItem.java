package invoice.com.demo.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "quotation_items")
public class QuotationItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quotation_id")
    private Quotation quotation;

    @Column(name = "product_id")
    private Long productId;

    private Integer quantity;
    @Column(name = "unit_price")
    private Double UnitPrice;
    @Column(name = "line_total")
    private Double lineTotal;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private  LocalDateTime deletedDate;

}
