package invoice.com.demo.domain;

import invoice.com.demo.audit.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="products")
public class Product extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String uuid;
    private String name;
    private String image_url;
    private BigDecimal price ;


    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.IN_STOCK; // កំណត់តម្លៃចាប់ផ្ដើមឱ្យច្បាស់

    private Boolean isDeleted = false;
    private String description;

    @Enumerated(EnumType.STRING)
    private Currency currency_type;


    // Many products → one type
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_type_id", nullable = false)
    private ProductType productType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private Stocks stock;



    // បន្ថែម Method នេះដើម្បី Update status តាមទិន្នន័យ Stock
    public void updateStatusFromStock() {
        if (this.stock == null || this.stock.getQuantity() == null || this.stock.getQuantity() <= 0) {
            this.status = ProductStatus.OUT_STOCK;
        } else if (this.stock.getLow_stock() != null && this.stock.getQuantity() <= this.stock.getLow_stock()) {
            this.status = ProductStatus.LOW_STOCK;
        } else {
            this.status = ProductStatus.IN_STOCK;
        }
    }

    @PostLoad
    protected void onLoad() {
        // ហៅ Logic គណនា Status ភ្លាមៗពេល Load ទិន្នន័យពី DB
        this.updateStatusFromStock();
    }

}
