package invoice.com.demo.mapper;

import invoice.com.demo.domain.User;
import invoice.com.demo.features.auth.dto.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User mapFromRegisterCreateRequest(RegisterRequest registerRequest);

}
