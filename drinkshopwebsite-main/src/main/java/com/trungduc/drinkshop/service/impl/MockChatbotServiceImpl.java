package com.trungduc.drinkshop.service.impl;

import com.trungduc.drinkshop.dto.chat.ChatbotResponse;
import com.trungduc.drinkshop.entity.Drink;
import com.trungduc.drinkshop.entity.Category;
import com.trungduc.drinkshop.repository.DrinkRepository;
import com.trungduc.drinkshop.repository.CategoryRepository;
import com.trungduc.drinkshop.service.ChatbotService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Primary
@ConditionalOnProperty(name = "openai.use.mock", havingValue = "true")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MockChatbotServiceImpl implements ChatbotService {

    DrinkRepository drinkRepository;
    CategoryRepository categoryRepository;

    private static final Map<String, String> PREDEFINED_RESPONSES = new HashMap<>();
    private static final Map<String, List<String>> PREDEFINED_KEYWORDS = new HashMap<>();

    static {
        // Initialize predefined responses
        PREDEFINED_RESPONSES.put(
                "hello|hi|xin chào|chào|hey",
                "Xin chào! Tôi là trợ lý đồ uống của Rooks Drinks. Bạn đang tìm kiếm đồ uống gì? Tôi có thể giúp bạn tìm đồ uống theo thể loại, tác giả, hoặc chủ đề bạn quan tâm."
        );

        PREDEFINED_RESPONSES.put(
                "recommend|đề xuất|gợi ý|suggest",
                "Dưới đây là một số đồ uống phổ biến mà tôi đề xuất cho bạn. Bạn có thể cho tôi biết thêm về sở thích đọc đồ uống của bạn để tôi có thể đưa ra gợi ý phù hợp hơn không?"
        );

        PREDEFINED_RESPONSES.put(
                "novel|tiểu thuyết|truyện",
                "Tiểu thuyết là một thể loại rất phổ biến. Dưới đây là một số tiểu thuyết hay mà chúng tôi có trong cửa hàng. Bạn có thích đọc tiểu thuyết lãng mạn, hành động, hay khoa học viễn tưởng không?"
        );

        PREDEFINED_RESPONSES.put(
                "science|khoa học|scientific",
                "Đồ Uống khoa học là lựa chọn tuyệt vời để mở rộng kiến thức. Dưới đây là một số đồ uống khoa học phổ biến trong cửa hàng của chúng tôi. Bạn quan tâm đến lĩnh vực khoa học cụ thể nào không?"
        );

        PREDEFINED_RESPONSES.put(
                "history|lịch sử|historical",
                "Đồ Uống lịch sử giúp chúng ta hiểu rõ hơn về quá khứ. Dưới đây là một số đồ uống lịch sử hay trong cửa hàng của chúng tôi. Bạn quan tâm đến lịch sử của quốc gia hay thời kỳ nào cụ thể không?"
        );

        PREDEFINED_RESPONSES.put(
                "self-help|self help|phát triển bản thân|kỹ năng sống",
                "Đồ Uống phát triển bản thân là công cụ tuyệt vời để cải thiện cuộc sống. Dưới đây là một số đồ uống phát triển bản thân phổ biến trong cửa hàng của chúng tôi."
        );

        PREDEFINED_RESPONSES.put(
                "children|trẻ em|kids|thiếu nhi",
                "Đồ Uống thiếu nhi là món quà tuyệt vời cho trẻ em. Dưới đây là một số đồ uống thiếu nhi phổ biến trong cửa hàng của chúng tôi. Bạn đang tìm đồ uống cho trẻ ở độ tuổi nào?"
        );

        PREDEFINED_RESPONSES.put(
                "business|kinh doanh|marketing|management|quản lý",
                "Đồ Uống kinh doanh giúp phát triển kỹ năng và kiến thức trong lĩnh vực này. Dưới đây là một số đồ uống kinh doanh phổ biến trong cửa hàng của chúng tôi."
        );

        PREDEFINED_RESPONSES.put(
                ".*",
                "Cảm ơn bạn đã chia sẻ. Dưới đây là một số đồ uống có thể phù hợp với yêu cầu của bạn. Nếu bạn muốn tìm đồ uống cụ thể hơn, hãy cho tôi biết thêm chi tiết về sở thích của bạn."
        );

        // Initialize predefined keywords
        PREDEFINED_KEYWORDS.put("hello|hi|xin chào|chào|hey", Arrays.asList("bestseller", "popular", "new"));
        PREDEFINED_KEYWORDS.put("recommend|đề xuất|gợi ý|suggest", Arrays.asList("bestseller", "popular", "new"));
        PREDEFINED_KEYWORDS.put("novel|tiểu thuyết|truyện", Arrays.asList("novel", "fiction", "story", "tiểu thuyết"));
        PREDEFINED_KEYWORDS.put("science|khoa học|scientific", Arrays.asList("science", "physics", "biology", "khoa học"));
        PREDEFINED_KEYWORDS.put("history|lịch sử|historical", Arrays.asList("history", "historical", "lịch sử"));
        PREDEFINED_KEYWORDS.put("self-help|self help|phát triển bản thân|kỹ năng sống", Arrays.asList("self-help", "motivation", "personal development", "phát triển"));
        PREDEFINED_KEYWORDS.put("children|trẻ em|kids|thiếu nhi", Arrays.asList("children", "kids", "thiếu nhi", "trẻ em"));
        PREDEFINED_KEYWORDS.put("business|kinh doanh|marketing|management|quản lý", Arrays.asList("business", "marketing", "management", "kinh doanh", "quản lý"));
        PREDEFINED_KEYWORDS.put(".*", Arrays.asList("bestseller", "popular", "new"));
    }

    @Override
    public ChatbotResponse processMessage(String message) {
        // Find matching predefined response
        String response = findMatchingResponse(message.toLowerCase());

        // Get keywords based on the message
        List<String> keywords = findMatchingKeywords(message.toLowerCase());

        // Add any potential keywords from the message itself
        keywords.addAll(extractKeywordsFromUserMessage(message));

        // Make keywords unique
        keywords = keywords.stream().distinct().collect(Collectors.toList());

        // First try to find drinks by category explicitly mentioned in the message
        List<Drink> recommendedDrinks = getDrinksByCategory(message);

        // If no drinks found by category, search using keywords
        if (recommendedDrinks.isEmpty()) {
            recommendedDrinks = searchDrinksByKeywords(keywords);
        }

        // If still no drinks, get popular drinks
        if (recommendedDrinks.isEmpty()) {
            // Check if the message contains a specific category or genre request
            if (containsCategoryRequest(message)) {
                return new ChatbotResponse(
                        "Xin lỗi, tôi không tìm thấy đồ uống nào phù hợp với thể loại bạn yêu cầu. " +
                                "Có thể chúng tôi chưa có đồ uống thuộc thể loại này hoặc bạn có thể thử tìm với từ khóa khác. " +
                                "Dưới đây là một số đồ uống phổ biến bạn có thể quan tâm:",
                        getPopularDrinks(5)
                );
            } else {
                // Generic no results found
                return new ChatbotResponse(
                        "Xin lỗi, tôi không tìm thấy đồ uống nào phù hợp với yêu cầu của bạn. " +
                                "Bạn có thể thử tìm với từ khóa khác hoặc xem một số đồ uống phổ biến dưới đây:",
                        getPopularDrinks(5)
                );
            }
        }

        // Customize response based on what was found
        if (isCategorySpecificQuery(message)) {
            String categoryName = extractCategoryName(message);
            if (!categoryName.isEmpty()) {
                response = "Dưới đây là một số đồ uống thuộc thể loại " + categoryName + " mà bạn có thể quan tâm:";
            }
        }

        return new ChatbotResponse(response, recommendedDrinks);
    }

    private String findMatchingResponse(String message) {
        for (Map.Entry<String, String> entry : PREDEFINED_RESPONSES.entrySet()) {
            if (Pattern.compile(entry.getKey()).matcher(message).find()) {
                return entry.getValue();
            }
        }
        return PREDEFINED_RESPONSES.get(".*"); // Default response
    }

    private List<String> findMatchingKeywords(String message) {
        for (Map.Entry<String, List<String>> entry : PREDEFINED_KEYWORDS.entrySet()) {
            if (Pattern.compile(entry.getKey()).matcher(message).find()) {
                return new ArrayList<>(entry.getValue());
            }
        }
        return new ArrayList<>(PREDEFINED_KEYWORDS.get(".*")); // Default keywords
    }

    // Add helper methods to detect category requests and extract category names

    private boolean containsCategoryRequest(String message) {
        String lowercaseMsg = message.toLowerCase();
        return lowercaseMsg.contains("thể loại") ||
                lowercaseMsg.contains("loại đồ uống") ||
                lowercaseMsg.contains("genre") ||
                lowercaseMsg.contains("chủ đề") ||
                isCategorySpecificQuery(message);
    }

    private boolean isCategorySpecificQuery(String message) {
        // Check if the message is asking for a specific category
        List<Category> categories = categoryRepository.findAll();
        String lowercaseMsg = message.toLowerCase();

        for (Category category : categories) {
            if (lowercaseMsg.contains(category.getName().toLowerCase())) {
                return true;
            }
        }

        // Check for common genre keywords
        String[] genreKeywords = {
                "tiểu thuyết", "truyện", "khoa học", "lịch sử", "kinh doanh",
                "tâm lý", "self-help", "thiếu nhi", "giáo dục", "văn học",
                "trinh thám", "kinh dị", "hài hước", "lãng mạn", "fantasy",
                "khoa học viễn tưởng", "sci-fi", "tiểu sử", "hồi ký", "tôn giáo",
                "tham khảo", "đồ uống giáo khoa", "truyện tranh", "manga", "comic"
        };

        for (String genre : genreKeywords) {
            if (lowercaseMsg.contains(genre)) {
                return true;
            }
        }

        return false;
    }

    private String extractCategoryName(String message) {
        // Try to extract category name from message
        List<Category> categories = categoryRepository.findAll();
        String lowercaseMsg = message.toLowerCase();

        for (Category category : categories) {
            String categoryName = category.getName().toLowerCase();
            if (lowercaseMsg.contains(categoryName)) {
                return category.getName();
            }
        }

        // Check for common genre keywords
        Map<String, String> genreKeywords = new HashMap<>();
        genreKeywords.put("tiểu thuyết", "Tiểu thuyết");
        genreKeywords.put("truyện", "Truyện");
        genreKeywords.put("khoa học", "Khoa học");
        genreKeywords.put("lịch sử", "Lịch sử");
        genreKeywords.put("kinh doanh", "Kinh doanh");
        genreKeywords.put("tâm lý", "Tâm lý");
        genreKeywords.put("self-help", "Phát triển bản thân");
        genreKeywords.put("thiếu nhi", "Thiếu nhi");
        genreKeywords.put("giáo dục", "Giáo dục");
        genreKeywords.put("văn học", "Văn học");
        genreKeywords.put("trinh thám", "Trinh thám");
        genreKeywords.put("kinh dị", "Kinh dị");
        genreKeywords.put("hài hước", "Hài hước");
        genreKeywords.put("lãng mạn", "Lãng mạn");
        genreKeywords.put("fantasy", "Fantasy");
        genreKeywords.put("khoa học viễn tưởng", "Khoa học viễn tưởng");
        genreKeywords.put("sci-fi", "Khoa học viễn tưởng");

        for (Map.Entry<String, String> entry : genreKeywords.entrySet()) {
            if (lowercaseMsg.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return "";
    }

    private List<Drink> getDrinksByCategory(String message) {
        // Try to find a category that matches the message
        List<Category> categories = categoryRepository.findAll();
        String lowercaseMsg = message.toLowerCase();

        for (Category category : categories) {
            if (lowercaseMsg.contains(category.getName().toLowerCase())) {
                List<Drink> drinks = drinkRepository.findTop4ByCategoryIdAndActiveFlag(category.getId(), true);
                if (!drinks.isEmpty()) {
                    return drinks;
                }
            }
        }

        // Check for common genre keywords if no category match
        Map<String, List<String>> genreKeywords = new HashMap<>();
        genreKeywords.put("tiểu thuyết", Arrays.asList("novel", "fiction", "tiểu thuyết"));
        genreKeywords.put("truyện", Arrays.asList("story", "truyện", "tales"));
        genreKeywords.put("khoa học", Arrays.asList("science", "khoa học", "scientific"));
        genreKeywords.put("lịch sử", Arrays.asList("history", "lịch sử", "historical"));
        genreKeywords.put("kinh doanh", Arrays.asList("business", "kinh doanh", "marketing"));
        genreKeywords.put("tâm lý", Arrays.asList("psychology", "tâm lý", "mental"));
        genreKeywords.put("self-help", Arrays.asList("self-help", "phát triển bản thân", "self improvement"));
        genreKeywords.put("thiếu nhi", Arrays.asList("children", "thiếu nhi", "kids", "trẻ em"));

        for (Map.Entry<String, List<String>> entry : genreKeywords.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (lowercaseMsg.contains(keyword)) {
                    // Search for drinks with this genre keyword
                    List<Drink> drinks = searchDrinksByKeywords(Collections.singletonList(entry.getKey()));
                    if (!drinks.isEmpty()) {
                        return drinks;
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    private List<Drink> getPopularDrinks(int limit) {
        return drinkRepository.findTop4ByActiveFlagOrderByBuyCountDesc(true);
    }

    @Override
    public List<Drink> searchDrinksByKeywords(List<String> keywords) {
        if (keywords.isEmpty()) {
            return new ArrayList<>();
        }

        // Get all active drinks
        List<Drink> allDrinks = drinkRepository.findAllByActiveFlag(true);

        // Filter drinks based on keywords
        return allDrinks.stream()
                .filter(drink -> matchesKeywords(drink, keywords))
                .limit(5) // Limit to 5 recommendations
                .collect(Collectors.toList());
    }

    private boolean matchesKeywords(Drink drink, List<String> keywords) {
        String drinkInfo = (drink.getTitle() + " " +
                drink.getAuthor() + " " +
                drink.getPublisher() + " " +
                (drink.getCategory() != null ? drink.getCategory().getName() : "") + " " +
                drink.getDescription()).toLowerCase();

        // Check if any of the keywords match the drink info
        for (String keyword : keywords) {
            if (drinkInfo.contains(keyword.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    private List<String> extractKeywordsFromUserMessage(String message) {
        // Simple keyword extraction - split by spaces and filter out common words
        Set<String> stopWords = new HashSet<>(Arrays.asList(
                "a", "an", "the", "and", "or", "but", "is", "are", "was", "were",
                "be", "been", "being", "have", "has", "had", "do", "does", "did",
                "to", "at", "by", "for", "with", "about", "against", "between", "into",
                "through", "during", "before", "after", "above", "below", "from", "up",
                "down", "in", "out", "on", "off", "over", "under", "again", "further",
                "then", "once", "here", "there", "when", "where", "why", "how", "all",
                "any", "both", "each", "few", "more", "most", "other", "some", "such",
                "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very",
                "can", "will", "just", "should", "now", "tôi", "bạn", "anh", "chị", "của",
                "và", "hoặc", "nhưng", "là", "có", "không", "đã", "sẽ", "đang", "cần",
                "muốn", "thích", "yêu", "ghét", "cho", "với", "về", "từ", "đến", "trong",
                "ngoài", "trên", "dưới", "khi", "nếu", "vì", "bởi", "tại", "sao", "làm",
                "gì", "ai", "hỏi", "trả lời", "giúp", "xin", "vui lòng", "cảm ơn", "xin chào",
                "tạm biệt", "gợi ý", "đề xuất", "đồ uống", "cuốn", "quyển", "đọc", "mua", "bán"
        ));

        return Arrays.stream(message.toLowerCase().split("\\s+"))
                .filter(word -> !stopWords.contains(word) && word.length() > 2)
                .distinct()
                .limit(5)
                .collect(Collectors.toList());
    }
}
