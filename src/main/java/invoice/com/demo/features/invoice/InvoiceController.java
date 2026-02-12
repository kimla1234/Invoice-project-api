package invoice.com.demo.features.invoice;

import invoice.com.demo.features.invoice.dto.InvoiceRequest;
import invoice.com.demo.features.invoice.dto.InvoiceResponse;
import invoice.com.demo.features.invoiceitems.dto.InvoiceItemRequest;
import invoice.com.demo.features.products.dto.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor

public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InvoiceResponse> create(@RequestBody InvoiceRequest invoiceRequest, @AuthenticationPrincipal Jwt jwt) {
        return invoiceService.create(invoiceRequest,jwt);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getById(@PathVariable Long id) {

        return invoiceService.getById(id);
    }

    @GetMapping
    public ResponseEntity<Page<InvoiceResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal Jwt jwt) {
        return invoiceService.getAll(jwt, page, size);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponse> update(@PathVariable Long id,@RequestBody InvoiceRequest request) {
        return invoiceService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
       return invoiceService.delete(id);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<InvoiceResponse> addItem(
            @PathVariable Long id,
            @RequestBody InvoiceItemRequest invoiceItemRequest) {
        return invoiceService.addItem(id, invoiceItemRequest);
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<String> removeItem(
            @PathVariable Long id,
            @PathVariable Long itemId) {
        return invoiceService.removeItem(id, itemId);
    }

}