package invoice.com.demo.features.clients;

import invoice.com.demo.domain.Client;
import invoice.com.demo.domain.User;
import invoice.com.demo.exception.DuplicatePhoneException;
import invoice.com.demo.features.clients.dto.ClientCreateRequest;
import invoice.com.demo.features.clients.dto.ClientResponse;
import invoice.com.demo.features.clients.dto.ClientUpdateRequest;
import invoice.com.demo.features.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService{
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    @Override
    public ClientResponse createClient(ClientCreateRequest request){

        User user =getCurrentUser();

        Client client = new Client();
        client.setName(request.name());
        client.setGender(request.gender());
        client.setPhoneNumber(request.phoneNumber());
        client.setAddress(request.address());
        client.setUser(user);

        Client saved= clientRepository.save(client);

        return mapToResponse(saved);
    }

    @Override
    public ClientResponse updateClient(Long id, ClientUpdateRequest request){
        User user = getCurrentUser();
        Client client = clientRepository.findByIdAndUser(id, user).orElseThrow(()->new RuntimeException("Client not found"));

        client.setName(request.name());
        client.setGender(request.gender());
        client.setPhoneNumber(request.phoneNumber());
        client.setAddress(request.address());

        Client saved = clientRepository.save(client);
        return mapToResponse(saved);
    }

    @Override
    public ClientResponse deleteClient (Long id){
        User user=getCurrentUser();
        Client client=clientRepository.findByIdAndUserAndDeletedAtIsNull(id, user).orElseThrow(()->new RuntimeException("Client not found"));

        client.setDeletedAt(LocalDateTime.now());
        Client saved = clientRepository.save(client);
        return mapToResponse(saved);
    }

    @Override
    public List<ClientResponse>getMyClients(){
        User user=getCurrentUser();
        return clientRepository.findByUserAndDeletedAtIsNull(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ClientResponse getClientById(Long id) {

        User user = getCurrentUser(); // from SecurityContext

        Client client = clientRepository.findByIdAndUserAndDeletedAtIsNull(id, user)
                .orElseThrow(() ->
                        new RuntimeException("Client not found"));

        return mapToResponse(client);
    }

    private User getCurrentUser() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName(); // comes from JWT subject

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    private ClientResponse mapToResponse(Client client){
        return new ClientResponse(
                client.getId(),
                client.getName(),
                client.getGender(),
                client.getPhoneNumber(),
                client.getAddress(),
                client.getCreatedAt(),
                client.getUpdatedAt(),
                client.getDeletedAt()
        );
    }
}
