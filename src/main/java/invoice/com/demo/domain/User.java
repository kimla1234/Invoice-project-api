package invoice.com.demo.domain;

import invoice.com.demo.audit.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false)
    private String name;

    @Column(length = 30)
    private String email;

    private String password;
    private String image_profile;

    @Column(length = 30)
    private String phone_number;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;

    private String verificationCode;

    private Boolean status = true;
    private Boolean isVerified = false;

    @OneToMany(mappedBy = "user")
    private List<Product> products;

}
