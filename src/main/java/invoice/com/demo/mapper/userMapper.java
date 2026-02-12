package invoice.com.demo.mapper;


import invoice.com.demo.domain.User;
import invoice.com.demo.features.users.dto.UserResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface userMapper {
    @Mapping(source = "phone_number", target = "phoneNumber")
    @Mapping(source = "image_profile", target = "imageProfile")
    UserResponse mapFromUserToUserResponse(User user);


    UserResponse toUserResponse(User updatedUser);
}
