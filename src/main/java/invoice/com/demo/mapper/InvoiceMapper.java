package invoice.com.demo.mapper;

import invoice.com.demo.domain.Invoice;
import invoice.com.demo.domain.User;
import invoice.com.demo.features.auth.dto.RegisterRequest;
import invoice.com.demo.features.invoice.dto.InvoiceRequest;
import invoice.com.demo.features.invoice.dto.InvoiceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
@Mapper(componentModel = "spring")

public interface InvoiceMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "client.id", target = "clientId")
    InvoiceResponse toResponse(Invoice invoice);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Invoice toEntity(InvoiceRequest request);
}