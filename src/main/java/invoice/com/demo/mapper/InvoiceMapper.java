package invoice.com.demo.mapper;

import invoice.com.demo.domain.Invoice;
import invoice.com.demo.features.invoice.dto.InvoiceRequest;
import invoice.com.demo.features.invoice.dto.InvoiceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = InvoiceItemMapper.class)  // Add uses
public interface InvoiceMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "items", target = "items")  // This will now use InvoiceItemMapper
    @Mapping(source = "issueDate", target = "issueDate")  // This will now use InvoiceItemMapper
    @Mapping(source = "expireDate", target = "expireDate")  // This will now use InvoiceItemMapper
    InvoiceResponse toResponse(Invoice invoice);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Invoice toEntity(InvoiceRequest request);
}