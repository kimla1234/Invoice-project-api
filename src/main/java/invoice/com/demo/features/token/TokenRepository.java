package invoice.com.demo.features.token;

import invoice.com.demo.domain.Token;
import invoice.com.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    Token findByUser(User user);

    @Modifying
    void deleteByUser(User user);
}
