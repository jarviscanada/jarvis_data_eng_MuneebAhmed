package ca.jrvs.apps.stockquote;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class QuoteHttpHelper {
    private String apiKey;
    private OkHttpClient client;
    private ObjectMapper mapper;

    public QuoteHttpHelper(String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
        this.mapper = new ObjectMapper();
    }

    public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException {
        try {
            String url = "https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&datatype=json";
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("X-RapidAPI-Key", apiKey)
                    .addHeader("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String jsonData = response.body().string();
                String quoteData = mapper.readTree(jsonData).path("Global Quote").toString();

                if (quoteData.isEmpty()) {
                    throw new IllegalArgumentException("No data found for the given symbol: " + symbol);
                }

                return mapper.readValue(quoteData, Quote.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }
}
