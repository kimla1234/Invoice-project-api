package invoice.com.demo.domain;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;



@Entity
@Table(name = "invoice_items")
@Data
public class InvoiceItems {
    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @OneToOne
    @JoinColumn(name = "product_id")
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
