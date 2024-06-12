package ca.jrvs.apps.trading.model.domain;
import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlphaVantageQuote {

    @JsonIgnore
    @JsonProperty("01. symbol")
    private String rawTicker;

    @JsonIgnore
    @JsonProperty("05. price")
    private Double rawLastPrice;

    private String ticker;
    private Double lastPrice;
    private Double bidPrice = 0.0;
    private Integer bidSize = 0;
    private Double askPrice = 0.0;
    private Integer askSize = 0;


    @JsonProperty("01. symbol")
    private void unpackTicker(String ticker) {
        this.rawTicker = ticker;
        this.ticker = ticker;
    }

    @JsonProperty("05. price")
    private void unpackLastPrice(Double lastPrice) {
        this.rawLastPrice = lastPrice;
        this.lastPrice = lastPrice;
    }


    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(Double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Integer getBidSize() {
        return bidSize;
    }

    public void setBidSize(Integer bidSize) {
        this.bidSize = bidSize;
    }

    public Double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(Double askPrice) {
        this.askPrice = askPrice;
    }

    public Integer getAskSize() {
        return askSize;
    }

    public void setAskSize(Integer askSize) {
        this.askSize = askSize;
    }

    @Override
    public String toString() {
        return "AlphaVantageQuote{" +
                "ticker='" + ticker + '\'' +
                ", lastPrice=" + lastPrice +
                ", bidPrice=" + bidPrice +
                ", bidSize=" + bidSize +
                ", askPrice=" + askPrice +
                ", askSize=" + askSize +
                '}';
    }
}
