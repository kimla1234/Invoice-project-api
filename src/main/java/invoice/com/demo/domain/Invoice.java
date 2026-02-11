package invoice.com.demo.domain;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "invoices")
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name ="subtotal")
    private double subtotal;

    @Column(name= "status", nullable = true)
    private String status;

    @Column(name = "grand_total")
    private double grandTotal;

    @Column(name = "tax")
    private double tax;

    @Column(nullable = true)
    private LocalDateTime issueDate;

    @Column(nullable = true)
    private LocalDateTime expireDate;

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

    @OneToMany(
            mappedBy = "invoice",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<InvoiceItem> items = new ArrayList<>();

    /// hellper
    // Helper method to add item
    public void addItem(InvoiceItem item) {
        items.add(item);
        item.setInvoice(this);
    }
//
//    // Helper method to remove item
    public void removeItem(InvoiceItem item) {
        items.remove(item);
        item.setInvoice(null);
    }
//
//
//
//    // Calculate totals from items
//    public void calculateTotals() {
//        double subtotalCalc = items.stream()
//                .mapToDouble(item -> item.getTotalPrice().doubleValue())
//                .sum();
//
//        this.subtotal = subtotalCalc;
//        this.grandTotal = subtotalCalc + this.tax;
//    }
}
