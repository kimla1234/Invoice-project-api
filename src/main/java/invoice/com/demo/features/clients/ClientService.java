package invoice.com.demo.features.clients;

import invoice.com.demo.features.clients.dto.ClientCreateRequest;
import invoice.com.demo.features.clients.dto.ClientResponse;
import invoice.com.demo.features.clients.dto.ClientUpdateRequest;

import java.util.List;

public interface ClientService {
    ClientResponse createClient(ClientCreateRequest request);
    List<ClientResponse>getMyClients();
    ClientResponse getClientById(Long id);
    ClientResponse updateClient(Long id, ClientUpdateRequest request);
    ClientResponse deleteClient(Long id);

}
