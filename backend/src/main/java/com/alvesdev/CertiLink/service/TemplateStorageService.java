package com.alvesdev.CertiLink.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TemplateStorageService {
    
    private final Path fileUploadDir = Paths.get("./uploads/templates").toAbsolutePath().normalize();

    public TemplateStorageService(){

        try{
            Files.createDirectories(this.fileUploadDir);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível criar o diretório de upload de templates.", e);
        }
    }

    public static final class StoredFileMeta {
        private final String fileName;
        private final String relativePath; // e.g. "uploads/templates/<file>"

        public StoredFileMeta(String fileName, String relativePath) {
            this.fileName = fileName;
            this.relativePath = relativePath;
        }

        public String getFileName() { return fileName; }
        public String getRelativePath() { return relativePath; }
    }

    public StoredFileMeta storeFile(MultipartFile templateFile){
        String original = templateFile.getOriginalFilename();
        String ext = ".bin";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.')).toLowerCase(Locale.ROOT);
        }
        String safeName = UUID.randomUUID().toString().replace("-", "") + ext;

        try {
            Path targetLocation = this.fileUploadDir.resolve(safeName);
            Files.copy(templateFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String rel = Paths.get("uploads", "templates", safeName).toString().replace('\\', '/');
            return new StoredFileMeta(safeName, rel);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível armazenar o arquivo do template.", e);
        }
    }
}
