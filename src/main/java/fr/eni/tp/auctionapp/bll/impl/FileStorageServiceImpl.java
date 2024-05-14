package fr.eni.tp.auctionapp.bll.impl;

import fr.eni.tp.auctionapp.bll.FileStorageService;
import fr.eni.tp.auctionapp.utils.URLUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${spring.servlet.multipart.location}")
    private String location;

    @Override
    public String store(MultipartFile file, int itemId) {
        try {
            String originalFilename = file.getOriginalFilename();
            int lastDotIndex = Objects.requireNonNull(originalFilename).lastIndexOf(".");
            String extension = originalFilename.substring(lastDotIndex + 1);
            String friendlyFilename = URLUtils.toFriendlyURL(originalFilename);

            String uniqueFilename =  friendlyFilename + itemId + "." + extension;

            Path rootLocation = Paths.get(location);
            Files.copy(file.getInputStream(), rootLocation.resolve(uniqueFilename));
            return uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    @Override
    public Resource load(String filename) {
        return null;
    }

    @Override
    public void delete(String filename) {

    }
}
