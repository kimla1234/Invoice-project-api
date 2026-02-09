package invoice.com.demo.features.invoice;

import invoice.com.demo.features.invoice.dto.InvoiceRequest;
import invoice.com.demo.features.invoice.dto.InvoiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface InvoiceService {
    ResponseEntity<InvoiceResponse> create(InvoiceRequest request, Jwt jwt);
    InvoiceResponse getById(Long id);
    List<InvoiceResponse> getAll(Jwt jwt);
    InvoiceResponse update(Long id, InvoiceRequest request);
    void delete(Long id);
}