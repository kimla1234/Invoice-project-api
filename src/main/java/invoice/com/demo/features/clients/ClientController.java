package invoice.com.demo.features.clients;

import invoice.com.demo.features.clients.dto.ClientCreateRequest;
import invoice.com.demo.features.clients.dto.ClientResponse;
import invoice.com.demo.features.clients.dto.ClientUpdateRequest;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client")
public class ClientController {
    private final ClientService clientService;

    @PostMapping("/create")
    public Map<String, Object> createClient(@Valid @RequestBody ClientCreateRequest request) {
        ClientResponse client = clientService.createClient(request);
        return Map.of("message", "Client created successfully", "data", client);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateClient(@PathVariable Long id, @Valid @RequestBody ClientUpdateRequest request){
        ClientResponse client = clientService.updateClient(id, request);
        return Map.of("message", "Client updated successfully", "data", client);
    }

    @GetMapping
    public List<ClientResponse>getMyClients(){
        return clientService.getMyClients();
    }

    @GetMapping("/{id}")
    public ClientResponse getClientById(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteClient(@PathVariable Long id) {
        ClientResponse deletedClient = clientService.deleteClient(id);
        return Map.of("message", "Client deleted successfully", "data", deletedClient);
    }
}
