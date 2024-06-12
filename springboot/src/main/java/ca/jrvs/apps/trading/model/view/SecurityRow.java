package ca.jrvs.apps.trading.model.view;

public class SecurityRow {
    private String ticker;
    private long position;
    private double marketValue;

    public SecurityRow(String ticker, long position, double marketValue) {
        this.ticker = ticker;
        this.position = position;
        this.marketValue = marketValue;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(double marketValue) {
        this.marketValue = marketValue;
    }
}
