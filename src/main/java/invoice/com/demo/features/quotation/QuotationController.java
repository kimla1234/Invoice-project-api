package invoice.com.demo.features.quotation;

import invoice.com.demo.features.quotation.dto.QuotationCreateRequest;
import invoice.com.demo.features.quotation.dto.QuotationResponse;
import invoice.com.demo.features.quotation.dto.QuotationUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quotations")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService quotationService;

    @PostMapping
    public QuotationResponse create(@RequestBody QuotationCreateRequest request) {
        return quotationService.createQuotation(request);
    }

    @PutMapping("/{id}")
    public QuotationResponse update(
            @PathVariable Long id,
            @RequestBody QuotationUpdateRequest request
    ) {
        request.setId(id);
        return quotationService.updateQuotation(request);
    }

    @GetMapping("/{id}")
    public QuotationResponse getById(@PathVariable Long id) {
        return quotationService.getById(id);
    }

    @GetMapping
    public List<QuotationResponse> getAll() {
        return quotationService.getAllQuotations();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        quotationService.deleteById(id);
    }
}
