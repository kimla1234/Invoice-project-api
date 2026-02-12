package invoice.com.demo.features.settings;

import invoice.com.demo.domain.Setting;
import invoice.com.demo.domain.User;
import invoice.com.demo.features.settings.dto.*;
import invoice.com.demo.features.users.dto.UserResponse;
import invoice.com.demo.features.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import invoice.com.demo.mapper.userMapper;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import invoice.com.demo.utils.PasswordValidator;
import invoice.com.demo.base.BaseMessage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;
    private final UserRepository userRepository;
    private final userMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SettingResponse getMySettings() {

        User user = getCurrentUser();

        Setting setting = settingRepository.findByUser(user)
                .orElseGet(() -> {
                    Setting newSetting = new Setting();
                    newSetting.setUser(user);
                    return settingRepository.save(newSetting);
                });

        return new SettingResponse(
                user.getId(),

                setting.getInvoiceFooter(),
                setting.getInvoiceNote(),
                setting.getSignatureUrl(),

                setting.getCompanyName(),
                setting.getCompanyEmail(),
                setting.getCompanyPhoneNumber(),
                setting.getCompanyAddress(),
                setting.getCompanyLogoUrl()
        );
    }

    @Override
    @Transactional
    public SettingResponse updateMySettings(SettingUpdateRequest request) {

        User user = getCurrentUser();

        Setting setting = settingRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Setting not found"));

        if (request.invoiceFooter() != null) {
            setting.setInvoiceFooter(request.invoiceFooter());
        }

        if (request.invoiceNote() != null) {
            setting.setInvoiceNote(request.invoiceNote());
        }

        if (request.signatureUrl() != null) {
            setting.setSignatureUrl(request.signatureUrl());
        }

        if (request.companyName() != null) {
            setting.setCompanyName(request.companyName());
        }

        if (request.companyPhoneNumber() != null) {
            setting.setCompanyPhoneNumber(request.companyPhoneNumber());
        }

        if (request.companyEmail() != null) {
            setting.setCompanyEmail(request.companyEmail());
        }

        if (request.companyAddress() != null) {
            setting.setCompanyAddress(request.companyAddress());
        }

        if (request.companyLogoUrl() != null) {
            setting.setCompanyLogoUrl(request.companyLogoUrl());
        }

        Setting saved = settingRepository.save(setting);

        return new SettingResponse(
                user.getId(),

                saved.getInvoiceFooter(),
                saved.getInvoiceNote(),
                saved.getSignatureUrl(),

                saved.getCompanyName(),
                saved.getCompanyEmail(),
                saved.getCompanyPhoneNumber(),
                saved.getCompanyAddress(),
                saved.getCompanyLogoUrl()
        );
    }

    @Override
    @Transactional
    public UserResponse updateMyProfile(UserProfileUpdateRequest request) {

        User user = getCurrentUser();

        if (request.name() != null) {
            user.setName(request.name());
        }

        if (request.phoneNumber() != null) {
            user.setPhone_number(request.phoneNumber());
        }

        if (request.imageProfile() != null) {
            user.setImage_profile(request.imageProfile());
        }

        User saved = userRepository.save(user);

        return userMapper.mapFromUserToUserResponse(saved);
    }

    @Override
    @Transactional
    public ChangePasswordResponse changeMyPassword(ChangePasswordRequest request) {

        User user = getCurrentUser();

        // 1️⃣ Check old password
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Old password is incorrect"
            );
        }

        // 2️⃣ Check new & confirm password
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "New password and confirm password do not match"
            );
        }

        // 3️⃣ (Optional but recommended) Validate password strength
        if (!PasswordValidator.validate(request.newPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character"
            );
        }

        // 4️⃣ Save new password
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        return new ChangePasswordResponse(
                user.getId(),
                "Password changed successfully"
        );
    }

    private User getCurrentUser() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName(); // from JWT subject

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    @Override
    @Transactional
    public SettingResponse uploadSignature(MultipartFile file) {

        User user = getCurrentUser();

        Setting setting = settingRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Setting not found"));

        if (file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Signature file is required"
            );
        }

        try {
            String uploadDir = "uploads/signatures/";
            Files.createDirectories(Paths.get(uploadDir));

            String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + filename);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // URL that frontend can use
            String fileUrl = "/uploads/signatures/" + filename;

            setting.setSignatureUrl(fileUrl);
            settingRepository.save(setting);

            return new SettingResponse(
                    user.getId(),
                    setting.getInvoiceFooter(),
                    setting.getInvoiceNote(),
                    fileUrl,
                    setting.getCompanyName(),
                    setting.getCompanyEmail(),
                    setting.getCompanyPhoneNumber(),
                    setting.getCompanyAddress(),
                    setting.getCompanyLogoUrl()
            );

        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to upload signature"
            );
        }
    }
    @Override
    public UserResponse getMyProfile() {
        User user = getCurrentUser();
        return userMapper.mapFromUserToUserResponse(user);
    }
    @Override
    @Transactional
    public UserResponse uploadProfileImage(MultipartFile file) {

        User user = getCurrentUser();

        if (file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Profile image is required"
            );
        }

        try {
            // folder: uploads/profile/
            String uploadDir = "uploads/profile/";
            Files.createDirectories(Paths.get(uploadDir));

            String filename =
                    UUID.randomUUID() + "-" + file.getOriginalFilename();

            Path filePath = Paths.get(uploadDir + filename);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // store ONLY URL in DB
            String fileUrl = "/uploads/profile/" + filename;

            user.setImage_profile(fileUrl);
            userRepository.save(user);

            return userMapper.mapFromUserToUserResponse(user);

        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to upload profile image"
            );
        }
    }
    @Override
    @Transactional
    public SettingResponse uploadCompanyLogo(MultipartFile file) {

        User user = getCurrentUser();

        if (file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Company logo is required"
            );
        }

        try {
            String uploadDir = "uploads/company/";
            Files.createDirectories(Paths.get(uploadDir));

            String filename =
                    UUID.randomUUID() + "-" + file.getOriginalFilename();

            Path filePath = Paths.get(uploadDir + filename);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            String fileUrl = "/uploads/company/" + filename;

            Setting setting = settingRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Setting not found"));

            setting.setCompanyLogoUrl(fileUrl);
            settingRepository.save(setting);

            return getMySettings();

        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to upload company logo"
            );
        }
    }
}
