package invoice.com.demo.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;



@Entity
@Table(name = "invoice_items")
@EntityListeners(AuditingEntityListener.class)  // ← ADD THIS
@Data
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    @JsonIgnore
    private Invoice invoice;

    @OneToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @Column(name = "unit_price")
    private double unitPrice;

    @Column(name = "quantity")
    private double quantity;

    @Column(name = "subtotal")
    private double subTotal;
//
//    @OneToOne
//    @JoinColumn(name = "quote_id", nullable = true)
//    private Quote quaote;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 🔹 automatically updated when UPDATE
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 🔹 soft delete (manual)
    @Column
    private LocalDateTime deletedAt;
}
