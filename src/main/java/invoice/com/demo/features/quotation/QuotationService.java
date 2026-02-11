package invoice.com.demo.features.quotation;

import invoice.com.demo.features.quotation.dto.QuotationCreateRequest;
import invoice.com.demo.features.quotation.dto.QuotationResponse;
import invoice.com.demo.features.quotation.dto.QuotationUpdateRequest;

import java.util.List;

public interface QuotationService {

    QuotationResponse createQuotation(QuotationCreateRequest request);

    QuotationResponse updateQuotation(QuotationUpdateRequest request);

    QuotationResponse getById(Long id);

    List<QuotationResponse> getAllQuotations();

    void deleteById(Long id);
}
