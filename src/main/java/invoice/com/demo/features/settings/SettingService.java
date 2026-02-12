package invoice.com.demo.features.settings;

import invoice.com.demo.features.settings.dto.*;
import invoice.com.demo.features.users.dto.UserResponse;
import invoice.com.demo.base.BaseMessage;
import org.springframework.web.multipart.MultipartFile;

public interface SettingService {
    SettingResponse getMySettings();
    SettingResponse updateMySettings (SettingUpdateRequest request);
    UserResponse getMyProfile();
    UserResponse updateMyProfile(UserProfileUpdateRequest request);
    UserResponse uploadProfileImage(MultipartFile file);
    ChangePasswordResponse changeMyPassword (ChangePasswordRequest request);
    SettingResponse uploadSignature(MultipartFile file);
    SettingResponse uploadCompanyLogo(MultipartFile file);

}
