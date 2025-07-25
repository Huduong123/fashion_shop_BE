package e_commerce.monolithic.service.admin;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadFile(MultipartFile file) throws IOException;
    boolean deleteFile(String fileName);
} 