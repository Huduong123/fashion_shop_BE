package e_commerce.monolithic.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Utility class for generating URL-friendly slugs from Vietnamese text
 */
public class SlugUtils {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGEWHITESPACE = Pattern.compile("(^\\s+|\\s+$)");

    /**
     * Generate a URL-friendly slug from Vietnamese text
     * Example: "Áo Polo Nam" -> "ao-polo-nam"
     * 
     * @param input the input text
     * @return the generated slug
     */
    public static String generateSlug(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        String slug = input.trim().toLowerCase();
        
        // Convert Vietnamese characters to ASCII
        slug = convertVietnameseToAscii(slug);
        
        // Remove edge whitespaces
        slug = EDGEWHITESPACE.matcher(slug).replaceAll("");
        
        // Replace whitespace with hyphens
        slug = WHITESPACE.matcher(slug).replaceAll("-");
        
        // Remove non-latin characters except hyphens
        slug = NONLATIN.matcher(slug).replaceAll("");
        
        // Remove multiple consecutive hyphens
        slug = slug.replaceAll("-+", "-");
        
        // Remove leading and trailing hyphens
        slug = slug.replaceAll("^-|-$", "");
        
        return slug;
    }

    /**
     * Convert Vietnamese characters to ASCII equivalents
     * 
     * @param input the input text with Vietnamese characters
     * @return the text with ASCII characters
     */
    private static String convertVietnameseToAscii(String input) {
        String result = input;
        
        // Vietnamese character mappings
        String[][] mappings = {
            // a variations
            {"á|à|ả|ã|ạ|ă|ắ|ằ|ẳ|ẵ|ặ|â|ấ|ầ|ẩ|ẫ|ậ", "a"},
            // A variations
            {"Á|À|Ả|Ã|Ạ|Ă|Ắ|Ằ|Ẳ|Ẵ|Ặ|Â|Ấ|Ầ|Ẩ|Ẫ|Ậ", "A"},
            // e variations
            {"é|è|ẻ|ẽ|ẹ|ê|ế|ề|ể|ễ|ệ", "e"},
            // E variations
            {"É|È|Ẻ|Ẽ|Ẹ|Ê|Ế|Ề|Ể|Ễ|Ệ", "E"},
            // i variations
            {"í|ì|ỉ|ĩ|ị", "i"},
            // I variations
            {"Í|Ì|Ỉ|Ĩ|Ị", "I"},
            // o variations
            {"ó|ò|ỏ|õ|ọ|ô|ố|ồ|ổ|ỗ|ộ|ơ|ớ|ờ|ở|ỡ|ợ", "o"},
            // O variations
            {"Ó|Ò|Ỏ|Õ|Ọ|Ô|Ố|Ồ|Ổ|Ỗ|Ộ|Ơ|Ớ|Ờ|Ở|Ỡ|Ợ", "O"},
            // u variations
            {"ú|ù|ủ|ũ|ụ|ư|ứ|ừ|ử|ữ|ự", "u"},
            // U variations
            {"Ú|Ù|Ủ|Ũ|Ụ|Ư|Ứ|Ừ|Ử|Ữ|Ự", "U"},
            // y variations
            {"ý|ỳ|ỷ|ỹ|ỵ", "y"},
            // Y variations
            {"Ý|Ỳ|Ỷ|Ỹ|Ỵ", "Y"},
            // d variations
            {"đ", "d"},
            {"Đ", "D"}
        };

        for (String[] mapping : mappings) {
            result = result.replaceAll(mapping[0], mapping[1]);
        }

        // Use Normalizer as fallback for any remaining special characters
        result = Normalizer.normalize(result, Normalizer.Form.NFD);
        result = result.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        return result;
    }

    /**
     * Generate a unique slug by appending a number if the slug already exists
     * 
     * @param baseSlug the base slug
     * @param existingSlugs set of existing slugs
     * @return a unique slug
     */
    public static String generateUniqueSlug(String baseSlug, java.util.Set<String> existingSlugs) {
        if (!existingSlugs.contains(baseSlug)) {
            return baseSlug;
        }

        int counter = 1;
        String uniqueSlug;
        do {
            uniqueSlug = baseSlug + "-" + counter;
            counter++;
        } while (existingSlugs.contains(uniqueSlug));

        return uniqueSlug;
    }
} 