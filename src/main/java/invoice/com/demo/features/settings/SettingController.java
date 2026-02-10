package invoice.com.demo.features.settings;

import invoice.com.demo.features.settings.dto.*;
import invoice.com.demo.features.users.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import invoice.com.demo.base.BaseMessage;
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping("/profile")
    public UserResponse getMyProfile() {
        return settingService.getMyProfile();
    }
    @PatchMapping("/password")
    public ChangePasswordResponse changeMyPassword(@RequestBody ChangePasswordRequest request){
        return settingService.changeMyPassword(request);
    }
    @PostMapping("/signature")
    public SettingResponse uploadSignature(@RequestParam("file") MultipartFile file) {
        return settingService.uploadSignature(file);
    }
    @PostMapping("/profile/image")
    public UserResponse uploadProfileImage(
            @RequestParam("file") MultipartFile file
    ) {
        return settingService.uploadProfileImage(file);
    }
    @PostMapping("/company/logo")
    public SettingResponse uploadCompanyLogo(
            @RequestParam("file") MultipartFile file
    ) {
        return settingService.uploadCompanyLogo(file);
    }
}
