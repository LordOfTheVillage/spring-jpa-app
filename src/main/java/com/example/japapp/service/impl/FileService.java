package com.example.japapp.service.impl;

import com.example.japapp.exception.MainException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

@Service
public class FileService {
    private final Path ROOT_LOCATION = Paths.get("uploads");

    public Resource findFile(String filename) {
        try {
            Path file = ROOT_LOCATION.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new MainException("Could not read file: " + filename);
            }
        } catch (Exception e) {
            throw new MainException("Could not read file: " + filename);
        }
    }
    public String saveFile(MultipartFile file, String... directoryPrefix) {
        if (directoryPrefix.length == 0) {
            directoryPrefix = new String[]{""};
        }
            try {
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null) {
                    throw new RuntimeException("File name is null");
                }
                Path directory = ROOT_LOCATION.resolve(directoryPrefix[0]);
                if (Files.notExists(directory)) {
                    Files.createDirectories(directory);
                }
                Path filePath = Path.of(directoryPrefix[0], generateName(file));
                Path absoluteFilePath = ROOT_LOCATION.resolve(filePath);
                Files.copy(file.getInputStream(), absoluteFilePath);
                return filePath.toString();
            } catch (IOException e) {
                throw new MainException("File can not be saved!");
            }
    }

    private String extractExtension(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        if (originalName == null) {
            return "";
        }
        String[] split = originalName.split("\\.");
        return split.length > 1 ? ("." + split[split.length - 1]) : "";
    }

    private String generateName(MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        String extension = extractExtension(file);
        return uuid + extension;
    }
}
