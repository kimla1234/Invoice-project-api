package invoice.com.demo.features.settings.dto;

public record SettingUpdateRequest(
        String invoiceFooter,
        String invoiceNote,
        String signatureUrl,

        String companyName,
        String companyEmail,
        String companyPhoneNumber,
        String companyAddress,
        String companyLogoUrl
) {
}
