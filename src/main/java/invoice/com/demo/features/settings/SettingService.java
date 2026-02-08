package invoice.com.demo.features.settings;

import invoice.com.demo.features.settings.dto.*;
import invoice.com.demo.features.users.dto.UserResponse;
import invoice.com.demo.base.BaseMessage;

public interface SettingService {
    SettingResponse getMySettings();
    SettingResponse updateMySettings (SettingUpdateRequest request);

    UserResponse updateMyProfile(UserProfileUpdateRequest request);
    ChangePasswordResponse changeMyPassword (ChangePasswordRequest request);
}
