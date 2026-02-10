package invoice.com.demo.features.invoice;

import invoice.com.demo.features.invoice.dto.InvoiceRequest;
import invoice.com.demo.features.invoice.dto.InvoiceResponse;
import invoice.com.demo.features.invoiceitems.dto.InvoiceItemRequest;
import invoice.com.demo.features.invoiceitems.dto.InvoiceItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface InvoiceService {
    ResponseEntity<InvoiceResponse> create(InvoiceRequest request, Jwt jwt);
    ResponseEntity<InvoiceResponse> getById(Long id);
    ResponseEntity<List<InvoiceResponse>> getAll(Jwt jwt);
    ResponseEntity<InvoiceResponse> update(Long id, InvoiceRequest request);
    ResponseEntity<Boolean> delete(Long id);
    ResponseEntity<String> removeItem(Long id,Long itemId);
    ResponseEntity<InvoiceResponse> addItem(Long id, InvoiceItemRequest invoiceItemRequest);
}