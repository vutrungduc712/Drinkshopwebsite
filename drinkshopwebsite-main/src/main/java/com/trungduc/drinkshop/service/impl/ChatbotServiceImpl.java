package com.trungduc.drinkshop.service.impl;

import com.trungduc.drinkshop.config.OpenAIConfig;
import com.trungduc.drinkshop.dto.chat.ChatMessage;
import com.trungduc.drinkshop.dto.chat.ChatRequest;
import com.trungduc.drinkshop.dto.chat.ChatResponse;
import com.trungduc.drinkshop.dto.chat.ChatbotResponse;
import com.trungduc.drinkshop.entity.Drink;
import com.trungduc.drinkshop.repository.DrinkRepository;
import com.trungduc.drinkshop.service.ChatbotService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ChatbotServiceImpl implements ChatbotService {

    RestTemplate restTemplate;
    OpenAIConfig openAIConfig;
    DrinkRepository drinkRepository;

    private static final Logger logger = LoggerFactory.getLogger(ChatbotServiceImpl.class);

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-3.5-turbo";

    @Override
    public ChatbotResponse processMessage(String message) {
        try {
            // Create a system prompt that instructs the AI to recommend drinks and extract keywords
            String systemPrompt = "You are a helpful assistant for a drinkstore called 'Rooks Drinks'. " +
                    "Your job is to recommend drinks to customers based on their queries. " +
                    "After your response, please include a list of keywords that can be used to search for drinks in our database. " +
                    "Format the keywords section like this: 'KEYWORDS: keyword1, keyword2, keyword3'. " +
                    "These keywords should be relevant to the user's query and likely to match drink titles, authors, or genres in our inventory.";

            // Create the chat request
            ChatRequest chatRequest = new ChatRequest(MODEL, String.valueOf(new ArrayList<>()));
            chatRequest.getMessages().add(new ChatMessage("system", systemPrompt));
            chatRequest.getMessages().add(new ChatMessage("user", message));

            // Set up headers with API key
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + openAIConfig.getOpenaiApiKey());

            // Create the HTTP entity
            HttpEntity<ChatRequest> entity = new HttpEntity<>(chatRequest, headers);

            // Make the API call
            ResponseEntity<ChatResponse> response = restTemplate.postForEntity(OPENAI_API_URL, entity, ChatResponse.class);

            // Process the response
            if (response.getBody() != null && !response.getBody().getChoices().isEmpty()) {
                String aiResponse = response.getBody().getChoices().get(0).getMessage().getContent();

                // Extract keywords from the response
                List<String> keywords = extractKeywords(aiResponse);

                // Remove the keywords section from the response
                String cleanResponse = removeKeywordsSection(aiResponse);

                // First try to find drinks by category explicitly mentioned in the message
                List<Drink> recommendedDrinks = getDrinksByCategory(message);

                // If no drinks found by category, search using keywords
                if (recommendedDrinks.isEmpty()) {
                    recommendedDrinks = searchDrinksByKeywords(keywords);
                }

                // If still no drinks found, handle the no-results case
                if (recommendedDrinks.isEmpty()) {
                    // Check if the message contains a specific category or genre request
                    if (containsCategoryRequest(message)) {
                        return new ChatbotResponse(
                                "Xin lỗi, tôi không tìm thấy đồ uống nào phù hợp với thể loại bạn yêu cầu. " +
                                        "Có thể chúng tôi chưa có đồ uống thuộc thể loại này hoặc bạn có thể thử tìm với từ khóa khác. " +
                                        cleanResponse + "\n\nDưới đây là một số đồ uống phổ biến bạn có thể quan tâm:",
                                getPopularDrinks(5)
                        );
                    } else {
                        // Generic no results found
                        return new ChatbotResponse(
                                "Xin lỗi, tôi không tìm thấy đồ uống nào phù hợp với yêu cầu của bạn. " +
                                        cleanResponse + "\n\nBạn có thể thử tìm với từ khóa khác hoặc xem một số đồ uống phổ biến dưới đây:",
                                getPopularDrinks(5)
                        );
                    }
                }

                // Create and return the chatbot response
                return new ChatbotResponse(cleanResponse, recommendedDrinks);
            }

            // Return a default response if something went wrong with the API response
            return getFallbackResponse(message);

        } catch (HttpClientErrorException e) {
            // Log the error
            logger.error("OpenAI API error: {}", e.getMessage());

            // Check if it's a quota error
            if (e.getRawStatusCode() == 429) {
                logger.error("OpenAI API quota exceeded");
                return getQuotaExceededResponse(message);
            }

            // For other API errors
            return getFallbackResponse(message);
        } catch (Exception e) {
            // Log the error
            logger.error("Error processing message: {}", e.getMessage(), e);

            // Return a fallback response for any other errors
            return getFallbackResponse(message);
        }
    }

// Add helper methods to detect category requests

    private boolean containsCategoryRequest(String message) {
        String lowercaseMsg = message.toLowerCase();
        return lowercaseMsg.contains("thể loại") ||
                lowercaseMsg.contains("loại đồ uống") ||
                lowercaseMsg.contains("genre") ||
                lowercaseMsg.contains("chủ đề");
    }

    private List<Drink> getDrinksByCategory(String message) {
        // Try to find a category that matches the message
        List<Drink> allDrinks = drinkRepository.findAllByActiveFlag(true);
        List<String> categoryNames = allDrinks.stream()
                .filter(drink -> drink.getCategory() != null)
                .map(drink -> drink.getCategory().getName().toLowerCase())
                .distinct()
                .collect(Collectors.toList());

        String lowercaseMsg = message.toLowerCase();

        for (String categoryName : categoryNames) {
            if (lowercaseMsg.contains(categoryName)) {
                return allDrinks.stream()
                        .filter(drink -> drink.getCategory() != null &&
                                drink.getCategory().getName().toLowerCase().equals(categoryName))
                        .limit(5)
                        .collect(Collectors.toList());
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
    /**
     * Creates a response for when the OpenAI API quota is exceeded
     */
    private ChatbotResponse getQuotaExceededResponse(String message) {
        String response = "Xin lỗi, hệ thống trợ lý đồ uống của chúng tôi đang tạm thời quá tải. " +
                "Tôi sẽ cố gắng tìm một số đồ uống phù hợp với yêu cầu của bạn dựa trên từ khóa.";

        // Extract potential keywords from the user message
        List<String> keywords = extractKeywordsFromUserMessage(message);

        // Get drink recommendations based on these keywords
        List<Drink> recommendedDrinks = searchDrinksByKeywords(keywords);

        return new ChatbotResponse(response, recommendedDrinks);
    }

    /**
     * Creates a generic fallback response when the API call fails
     */
    private ChatbotResponse getFallbackResponse(String message) {
        String response = "Xin lỗi, tôi không thể xử lý yêu cầu của bạn lúc này. " +
                "Dưới đây là một số đồ uống phổ biến mà bạn có thể quan tâm:";

        // Extract potential keywords from the user message
        List<String> keywords = extractKeywordsFromUserMessage(message);

        // If we couldn't extract keywords, get popular drinks
        List<Drink> recommendedDrinks;
        if (keywords.isEmpty()) {
            recommendedDrinks = getPopularDrinks(5);
        } else {
            recommendedDrinks = searchDrinksByKeywords(keywords);

            // If no drinks found with keywords, get popular drinks
            if (recommendedDrinks.isEmpty()) {
                recommendedDrinks = getPopularDrinks(5);
            }
        }

        return new ChatbotResponse(response, recommendedDrinks);
    }

    /**
     * Get popular drinks as a fallback
     */
    private List<Drink> getPopularDrinks(int limit) {
        return drinkRepository.findTop4ByActiveFlagOrderByBuyCountDesc(true);
    }

    /**
     * Extract potential keywords from the user's message
     */
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

    private List<String> extractKeywords(String response) {
        // Extract keywords using regex
        Pattern pattern = Pattern.compile("KEYWORDS:\\s*(.+)$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(response);

        if (matcher.find()) {
            String keywordsStr = matcher.group(1);
            return Arrays.stream(keywordsStr.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    private String removeKeywordsSection(String response) {
        // Remove the keywords section from the response
        return response.replaceAll("KEYWORDS:\\s*.+$", "").trim();
    }
}
