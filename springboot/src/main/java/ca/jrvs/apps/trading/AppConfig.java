package ca.jrvs.apps.trading;
import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    private Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Value("${app.ALPHA_VANTAGE_HOST}")
    private String alphaVantageHost;

    @Value("${app.ALPHA_VANTAGE_API_KEY}")
    private String alphaVantageApiKey;

    @Value("${app.RAPIDAPI_HOST}")
    private String rapidApiHost;


    @Bean
    public MarketDataConfig marketDataConfig() {
        MarketDataConfig marketDataConfig = new MarketDataConfig();
        marketDataConfig.setHost(alphaVantageHost);
        marketDataConfig.setToken(alphaVantageApiKey);
        marketDataConfig.setRapidApiHost(rapidApiHost);
        return marketDataConfig;
    }



    @Bean
    public HttpClientConnectionManager httpClientConnectionManager() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(50);
        cm.setDefaultMaxPerRoute(50);
        return cm;
    }
}
