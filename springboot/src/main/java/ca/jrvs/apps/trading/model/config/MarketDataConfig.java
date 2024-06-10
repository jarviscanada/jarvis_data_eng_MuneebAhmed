package ca.jrvs.apps.trading.model.config;

public class MarketDataConfig {
    private String host;
    private String token;
    private String rapidApiHost;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRapidApiHost() {
        return rapidApiHost;
    }

    public void setRapidApiHost(String rapidApiHost) {
        this.rapidApiHost = rapidApiHost;
    }

}
