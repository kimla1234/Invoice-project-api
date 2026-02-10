package invoice.com.demo.features.media;


import invoice.com.demo.features.media.dto.MediaResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MediaService {

    MediaResponse uploadSingle(MultipartFile file, String folderName);
}
