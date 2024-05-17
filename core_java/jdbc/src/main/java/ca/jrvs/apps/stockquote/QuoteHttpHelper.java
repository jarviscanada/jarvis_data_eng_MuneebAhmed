package ca.jrvs.apps.stockquote;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class QuoteHttpHelper {
    private static final Logger logger = LoggerFactory.getLogger(QuoteHttpHelper.class);
    private String apiKey;
    private OkHttpClient client;
    private ObjectMapper mapper;


    public QuoteHttpHelper(String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
        this.mapper = new ObjectMapper();
    }

    public Quote fetchQuoteInfo(String symbol) throws IOException {
        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey + "&datatype=json";
        logger.info("Fetching quote for symbol: {}", symbol);
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.error("Failed to fetch quote for symbol: {}. Response code: {}", symbol, response.code());
                throw new IOException("Unexpected code " + response + " for " + symbol);
            }
            String jsonData = response.body().string();
            logger.debug("Received JSON data: {}", jsonData);
            return parseQuote(jsonData, symbol);
        }
    }

    private Quote parseQuote(String jsonData, String symbol) throws IOException {
        if (jsonData == null || jsonData.isEmpty()) {
            logger.warn("No data found for the given symbol: {}", symbol);
            throw new IllegalArgumentException("No data found for the given symbol: " + symbol);
        }
        JsonNode rootNode = mapper.readTree(jsonData);
        JsonNode quoteNode = rootNode.path("Global Quote");
        if (quoteNode.isMissingNode()) {
            logger.warn("No quote data available for {}", symbol);
            throw new IllegalArgumentException("No quote data available for " + symbol);
        }
        return mapper.treeToValue(quoteNode, Quote.class);
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }
}
