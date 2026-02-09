package invoice.com.demo.domain;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;



@Entity
@Table(name = "invoices")
@Data
public class Invoice {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name ="subtotal")
    private double subtotal;

    @Column(name = "grand_total")
    private double grandTotal;

    @Column(name = "tax")
    private double tax;

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
