package invoice.com.demo.features.settings.dto;

public record SettingUpdateRequest(
        String invoiceFooter,
        String invoiceNote,
        String signatureUrl,

        String companyName,
        String companyPhoneNumber,
        String companyEmail,
        String companyAddress,
        String companyLogoUrl
) {
}
