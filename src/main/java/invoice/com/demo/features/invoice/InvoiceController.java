package invoice.com.demo.features.invoice;

import invoice.com.demo.features.invoice.dto.InvoiceRequest;
import invoice.com.demo.features.invoice.dto.InvoiceResponse;
import invoice.com.demo.features.products.dto.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public InvoiceResponse create(@RequestBody InvoiceRequest invoiceRequest, @AuthenticationPrincipal Jwt jwt) {
        return invoiceService.create(invoiceRequest,jwt);
    }
//
//    @GetMapping("/{id}")
//    public InvoiceResponse getById(@PathVariable Long id) {
//        return invoiceService.getById(id);
//    }
//
//    @GetMapping
//    public List<InvoiceResponse> getAll() {
//        return invoiceService.getAll();
//    }
//
//    @PutMapping("/{id}")
//    public InvoiceResponse update(@PathVariable Long id,
//                                  @RequestBody InvoiceRequest request) {
//        return invoiceService.update(id, request);
//    }
//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void delete(@PathVariable Long id) {
//        invoiceService.delete(id);
//    }
}