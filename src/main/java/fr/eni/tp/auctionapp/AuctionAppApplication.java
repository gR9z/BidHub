package fr.eni.tp.auctionapp;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication()
public class AuctionAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuctionAppApplication.class, args);
    }
}
