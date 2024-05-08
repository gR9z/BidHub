package fr.eni.tp.auctionapp.utils;

import java.text.Normalizer;
import java.util.Locale;

public class URLUtils {

    public static String toFriendlyURL(String url) {
        String normalized = Normalizer.normalize(url, Normalizer.Form.NFD);
        String slug = normalized.replaceAll("[\\u0300-\\u036F]", ""); // Remove accents
        slug = slug.toLowerCase(Locale.ENGLISH);
        slug = slug.replaceAll("[^a-z0-9]+", "-"); // Replace non-alphanumeric characters with hyphen
        slug = slug.replaceAll("-{2,}", "-"); // Replace multiple hyphens with a single one
        slug = slug.replaceAll("^-|-$", ""); // Trim hyphens at start and end of the string
        return slug;
    }
}
