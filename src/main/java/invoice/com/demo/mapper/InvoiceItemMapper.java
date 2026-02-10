package invoice.com.demo.mapper;

import invoice.com.demo.domain.InvoiceItem;
import invoice.com.demo.features.invoiceitems.dto.InvoiceItemRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface InvoiceItemMapper {


    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "product", ignore = true)
    InvoiceItem toEntity(InvoiceItemRequest request);
}
