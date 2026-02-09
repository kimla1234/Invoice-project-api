package invoice.com.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@Entity
@Table(name = "quotations")
public class Quotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "invoice_id")
    private Long invoiceId;

    private LocalDateTime quotationDate;
    private LocalDateTime quotationExpire;

    @Column(name = "total_amount")
    private Double totalAmount;

    @OneToMany(mappedBy = "quotation", orphanRemoval = true)
    private List<QuotationItem> item = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
