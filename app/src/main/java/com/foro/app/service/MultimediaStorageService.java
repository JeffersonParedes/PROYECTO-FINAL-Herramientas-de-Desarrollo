package com.foro.app.service;

import com.foro.app.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class MultimediaStorageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file, String type) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new BadRequestException("Nombre de archivo inválido");
        }

        String ext = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
        validarFormato(ext, type);

        // Generate unique filename to prevent conflicts
        String newFilename = UUID.randomUUID().toString() + "." + ext;

        try {
            Path uploadsDirectory = Paths.get(uploadDir);
            Files.createDirectories(uploadsDirectory);
            Path filePath = uploadsDirectory.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + newFilename;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar archivo multimedia: " + e.getMessage(), e);
        }
    }

    private void validarFormato(String ext, String type) {
        switch (type) {
            case "imagen":
                if (!ext.matches("png|jpg|jpeg|gif|webp")) {
                    throw new BadRequestException("Formato de imagen no válido: " + ext);
                }
                break;
            case "video":
                if (!ext.matches("mp4|webm|avi|mov")) {
                    throw new BadRequestException("Formato de video no válido: " + ext);
                }
                break;
            case "audio":
                if (!ext.matches("mp3|wav|ogg|aac")) {
                    throw new BadRequestException("Formato de audio no válido: " + ext);
                }
                break;
            default:
                throw new BadRequestException("Tipo de multimedia no soportado");
        }
    }
}