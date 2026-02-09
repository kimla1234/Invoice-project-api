package invoice.com.demo.features.quotation;

import invoice.com.demo.domain.Quotation;
import invoice.com.demo.domain.QuotationItem;
import invoice.com.demo.features.quotation.dto.QuotationCreateRequest;
import invoice.com.demo.features.quotation.dto.QuotationItemRequest;
import invoice.com.demo.features.quotation.dto.QuotationItemResponse;
import invoice.com.demo.features.quotation.dto.QuotationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuotationServiceImpl implements QuotationService {
    private final QuotationItemRepository quotationItemRepository;

    @Override
    public QuotationResponse createQuotation(QuotationCreateRequest request) {
        Quotation quotation = new Quotation();
        quotation.setUserId(request.getUserId());
        quotation.setClientId(request.getClientId());
        quotation.setInvoiceId(request.getInvoiceId());
        quotation.setQuotationDate(request.getQuotationDate());
        quotation.setQuotationExpire(request.getQuotationExpire());
        quotation.setCreatedAt(LocalDateTime.now());

        double totalAmount = 0;

        for (QuotationItemRequest itemRequest : request.getItems()) {
            QuotationItem quotationItem = new QuotationItem();
            quotationItem.setQuotation(quotation);
            quotationItem.setProductId(itemRequest.getProductId());
            quotationItem.setQuantity(itemRequest.getQuantity());
            quotationItem.setUnitPrice(itemRequest.getUnitPrice());

            double lineTotal = itemRequest.getQuantity() * itemRequest.getUnitPrice();
            itemRequest.setLineTotal(lineTotal);

            totalAmount += lineTotal;
            quotation.getItem().add(quotationItem);
        }
        quotation.setTotalAmount(totalAmount);
        return mapToResponse(quotation);
    }


    private QuotationResponse mapToResponse(Quotation quotation) {

        QuotationResponse res = new QuotationResponse();
        res.setId(quotation.getId());
        res.setUserId(quotation.getUserId());
        res.setClientId(quotation.getClientId());
        res.setInvoiceId(quotation.getInvoiceId());
        res.setQuotationDate(quotation.getQuotationDate());
        res.setQuotationExpire(quotation.getQuotationExpire());
        res.setTotalAmount(quotation.getTotalAmount());
        res.setCreatedAt(quotation.getCreatedAt());
        res.setUpdatedAt(quotation.getUpdatedAt());

        List<QuotationItemResponse> items = quotation.getItem().stream()
                .map(item -> {
                    QuotationItemResponse ir = new QuotationItemResponse();
                    ir.setId(item.getId());
                    ir.setQuotationId(quotation.getId());
                    ir.setProductId(item.getProductId());
                    ir.setQuantity(item.getQuantity());
                    ir.setUnitPrice(item.getUnitPrice());
                    ir.setLineTotal(item.getLineTotal());
                    return ir;
                }).toList();
        res.setItems(items);
        return res;
    }


    @Override
    public QuotationResponse updateQuotation(QuotationItemRequest request) {
        return null;
    }

    @Override
    public QuotationResponse getById(Long id) {
        return null;
    }

    @Override
    public List<QuotationResponse> getAllQuotations() {
        return List.of();
    }

    @Override
    public void deleteById(Long id) {

    }
}
