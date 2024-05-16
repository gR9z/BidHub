package fr.eni.tp.auctionapp.bll.impl;

import fr.eni.tp.auctionapp.bll.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path location;

    public FileStorageServiceImpl() {
        this.location = Paths.get("src/main/resources/static/images/products");
    }

    @Override
    public String store(MultipartFile file) {
        if(file == null) {
            return null;
        }

        String fileName = UUID.randomUUID() + Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, this.location.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    @Override
    public Resource load(String filename) {
        return null;
    }

    @Override
    public void delete(String filename) {}
}
