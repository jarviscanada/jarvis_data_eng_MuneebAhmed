package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.AlphaVantageQuote;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Repository;

@Repository
public class MarketDataDao {

    private static final String ALPHA_VANTAGE_FUNCTION = "GLOBAL_QUOTE";
    private final String alphaVantageUrl;
    private final String rapidApiHost;
    private final String rapidApiKey;



    private Logger logger = LoggerFactory.getLogger(MarketDataDao.class);
    private HttpClientConnectionManager httpClientConnectionManager;
    private ObjectMapper objectMapper;

    @Autowired
    public MarketDataDao(HttpClientConnectionManager httpClientConnectionManager,
                         MarketDataConfig marketDataConfig) {
        this.httpClientConnectionManager = httpClientConnectionManager;
        this.alphaVantageUrl = marketDataConfig.getHost() + "?function=" + ALPHA_VANTAGE_FUNCTION + "&symbol=%s&apikey=" + marketDataConfig.getToken();
        this.rapidApiHost = marketDataConfig.getRapidApiHost();
        this.rapidApiKey = marketDataConfig.getToken();
        this.objectMapper = new ObjectMapper();
    }

    public Optional<AlphaVantageQuote> findById(String ticker) {
        List<AlphaVantageQuote> quotes = findAllById(Collections.singletonList(ticker));
        if (quotes.isEmpty()) {
            return Optional.empty();
        } else if (quotes.size() == 1) {
            return Optional.of(quotes.get(0));
        } else {
            throw new DataRetrievalFailureException("Unexpected number of quotes");
        }
    }

    public List<AlphaVantageQuote> findAllById(Iterable<String> tickers) {
        List<String> tickersInputList = new LinkedList<>();
        tickers.iterator().forEachRemaining(tickersInputList::add);

        if (tickersInputList.isEmpty()) {
            throw new IllegalArgumentException("No tickers provided.");
        }

        List<AlphaVantageQuote> quotesOutput = new LinkedList<>();
        for (String ticker : tickersInputList) {
            String url = String.format(alphaVantageUrl, ticker);

            String responseString = executeHttpGet(url)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid ticker"));

            try {
                JsonNode root = objectMapper.readTree(responseString);
                JsonNode globalQuoteNode = root.path("Global Quote");

                if (globalQuoteNode.isMissingNode() || globalQuoteNode.size() == 0) {
                    throw new IllegalArgumentException("No quote found for ticker: " + ticker);
                }

                AlphaVantageQuote quote = objectMapper.treeToValue(globalQuoteNode, AlphaVantageQuote.class);
                quotesOutput.add(quote);
            } catch (IOException e) {
                throw new IllegalArgumentException("Failure parsing response", e);
            }
        }
        return quotesOutput;

    }

    private Optional<String> executeHttpGet(String url) {
        try (CloseableHttpClient httpClient = getHttpClient()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader(new BasicHeader("x-rapidapi-key", rapidApiKey));
            httpGet.setHeader(new BasicHeader("x-rapidapi-host", rapidApiHost));
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                StatusLine statusLine = response.getStatusLine();
                switch (statusLine.getStatusCode()) {
                    case 200:
                        HttpEntity entity = response.getEntity();
                        return Optional.of(EntityUtils.toString(entity));
                    case 404:
                        return Optional.empty();
                    default:
                        throw new DataRetrievalFailureException("Unexpected status code: " + statusLine.getStatusCode());
                }
            }
        } catch (IOException e) {
            throw new DataRetrievalFailureException("HTTP request failed", e);
        }
    }

    private CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .setConnectionManager(httpClientConnectionManager)
                .setConnectionManagerShared(true)
                .build();
    }
}