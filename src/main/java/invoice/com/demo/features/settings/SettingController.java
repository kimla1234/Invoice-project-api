package invoice.com.demo.features.settings;

import invoice.com.demo.features.settings.dto.*;
import invoice.com.demo.features.users.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import invoice.com.demo.base.BaseMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/setting")
public class SettingController {
    private final SettingService settingService;

    @GetMapping
    public SettingResponse getMySettings(){
        return settingService.getMySettings();
    }
    @PatchMapping
    public SettingResponse updateMySettings(@RequestBody SettingUpdateRequest request){
        return settingService.updateMySettings(request);

    }
    @PatchMapping("/profile")
    public UserResponse updateMyProfile(@RequestBody UserProfileUpdateRequest request){
        return settingService.updateMyProfile(request);
    }
    @PatchMapping("/password")
    public ChangePasswordResponse changeMyPassword(@RequestBody ChangePasswordRequest request){
        return settingService.changeMyPassword(request);
    }
}
