package invoice.com.demo.features.quotation;

import invoice.com.demo.domain.Quotation;
import invoice.com.demo.domain.QuotationItem;
import invoice.com.demo.domain.QuotationStatus;
import invoice.com.demo.features.quotation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuotationServiceImpl implements QuotationService {

    private final QuotationRepository quotationRepository;
    private QuotationStatus status;

    @Override
    public QuotationResponse createQuotation(QuotationCreateRequest request) {

        Quotation quotation = new Quotation();
        quotation.setUserId(request.getUserId());
        quotation.setClientId(request.getClientId());
        quotation.setInvoiceId(request.getInvoiceId());
        quotation.setQuotationNo(request.getQuotationNo());
        quotation.setQuotationDate(request.getQuotationDate());
        quotation.setQuotationExpire(request.getQuotationExpire());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (QuotationItemRequest itemReq : request.getItems()) {

            QuotationItem item = new QuotationItem();
            item.setQuotation(quotation);
            item.setProductId(itemReq.getProductId());
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(itemReq.getUnitPrice());

            BigDecimal lineTotal =
                    itemReq.getUnitPrice().multiply(
                            BigDecimal.valueOf(itemReq.getQuantity())
                    );

            item.setLineTotal(lineTotal);
            totalAmount = totalAmount.add(lineTotal);

            quotation.getItems().add(item);
        }

        if (this.status == null) {
            this.status = QuotationStatus.PENDING;
        }

        quotation.setTotalAmount(totalAmount);

        quotationRepository.save(quotation);

        return mapToResponse(quotation);
    }

    @Override
    public QuotationResponse updateQuotation(QuotationUpdateRequest request) {

        Quotation quotation = quotationRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Quotation not found"));

        quotation.setUserId(request.getUserId());
        quotation.setClientId(request.getClientId());
        quotation.setQuotationDate(request.getQuotationDate());
        quotation.setQuotationExpire(request.getQuotationExpire());
        quotation.setStatus(request.getStatus());
        quotation.getItems().clear();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (QuotationItemUpdateRequest itemReq : request.getItems()) {

            QuotationItem item = new QuotationItem();
            item.setQuotation(quotation);
            item.setProductId(itemReq.getProductId());
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(itemReq.getUnitPrice());

            BigDecimal lineTotal =
                    itemReq.getUnitPrice().multiply(
                            BigDecimal.valueOf(itemReq.getQuantity())
                    );

            item.setLineTotal(lineTotal);
            totalAmount = totalAmount.add(lineTotal);

            quotation.getItems().add(item);
        }

        quotation.setTotalAmount(totalAmount);

        quotationRepository.save(quotation);

        return mapToResponse(quotation);
    }

    @Override
    public QuotationResponse getById(Long id) {
        Quotation quotation = quotationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quotation not found"));

        return mapToResponse(quotation);
    }

    @Override
    public List<QuotationResponse> getAllQuotations() {
        return quotationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }



    @Override
    public void deleteById(Long id) {
        quotationRepository.deleteById(id);
    }

    /* =========================
       MAPPER
       ========================= */
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
        res.setStatus(quotation.getStatus());

        List<QuotationItemResponse> items = quotation.getItems().stream()
                .map(item -> {
                    QuotationItemResponse ir = new QuotationItemResponse();
                    ir.setId(item.getId());
                    ir.setProductId(item.getProductId());
                    ir.setProductName(item.getProductName());
                    ir.setQuantity(item.getQuantity());
                    ir.setUnitPrice(item.getUnitPrice());
                    ir.setLineTotal(item.getLineTotal());
                    return ir;
                }).toList();

        res.setItems(items);
        return res;
    }
}