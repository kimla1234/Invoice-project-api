package invoice.com.demo.features.clients;

import invoice.com.demo.domain.Client;
import invoice.com.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client>findByUser(User user);
    Optional<Client> findByIdAndUser(Long id, User user);

    List<Client> findByUserAndDeletedAtIsNull(User user);
    Optional<Client> findByIdAndUserAndDeletedAtIsNull(Long id, User user);
}
