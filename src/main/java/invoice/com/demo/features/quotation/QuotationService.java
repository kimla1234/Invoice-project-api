package invoice.com.demo.features.quotation;


import invoice.com.demo.features.quotation.dto.QuotationCreateRequest;
import invoice.com.demo.features.quotation.dto.QuotationItemRequest;
import invoice.com.demo.features.quotation.dto.QuotationResponse;

import java.util.List;

public interface QuotationService {
    QuotationResponse createQuotation(QuotationCreateRequest request);
    QuotationResponse updateQuotation(QuotationItemRequest request);
    QuotationResponse getById(Long id);

    List<QuotationResponse> getAllQuotations();
    void deleteById(Long id);
}
