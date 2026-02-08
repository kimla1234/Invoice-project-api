package invoice.com.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="settings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column
    private String invoiceFooter;

    @Column
    private String invoiceNote;

    @Column
    private String signatureUrl;

    @Column
    private String companyName;

    @Column
    private String companyPhoneNumber;

    @Column
    private String companyEmail;

    @Column
    private String companyAddress;

    @Column
    private  String companyLogoUrl;

}
