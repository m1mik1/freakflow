package com.freakflow.backend.application.service;



import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.*;
import java.util.UUID;

@Service
public class AvatarService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.base-url}")
    private String baseUrl;

    public String store(MultipartFile file, Long userId) throws IOException {
        // валидируем тип/размер, если нужно
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String filename = "avatar-" + userId + "-" + UUID.randomUUID() + "." + ext;

        Path target = Paths.get(uploadDir).resolve(filename);
        Files.createDirectories(target.getParent());
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        // публичный URL
        return baseUrl + "/uploads/avatars/" + filename;
    }
}
