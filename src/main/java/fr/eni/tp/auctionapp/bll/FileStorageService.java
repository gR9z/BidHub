package fr.eni.tp.auctionapp.bll;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String store(MultipartFile file, int itemId);
    Resource load(String filename);
    void delete(String filename);
}
