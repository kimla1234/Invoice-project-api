package invoice.com.demo.utils;

public class MediaUtil {

    public static String extractExtension(String name) {
        // extract extension from file
        int lastDotIndex = name
                .lastIndexOf(".");

        return name
                .substring(lastDotIndex + 1);
    }

}
