package invoice.com.demo.features.invoiceitems;

import invoice.com.demo.domain.InvoiceItem;

import java.util.List;



public interface InvoiceItemService {
    List<InvoiceItem> getAllByInvoiceId();

}
