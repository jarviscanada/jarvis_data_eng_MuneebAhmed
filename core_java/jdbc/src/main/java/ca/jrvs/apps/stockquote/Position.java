package ca.jrvs.apps.stockquote;

import java.util.Objects;

public class Position {
    private String ticker;
    private int numOfShares;
    private double valuePaid;

    public Position(String ticker, int numOfShares, double valuePaid) {
        this.ticker = ticker;
        this.numOfShares = numOfShares;
        this.valuePaid = valuePaid;
    }

    public Position() {

    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getNumOfShares() {
        return numOfShares;
    }

    public void setNumOfShares(int numOfShares) {
        this.numOfShares = numOfShares;
    }

    public double getValuePaid() {
        return valuePaid;
    }

    public void setValuePaid(double valuePaid) {
        this.valuePaid = valuePaid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (numOfShares != position.numOfShares) return false;
        if (Double.compare(position.valuePaid, valuePaid) != 0) return false;
        return Objects.equals(ticker, position.ticker);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = ticker != null ? ticker.hashCode() : 0;
        result = 31 * result + numOfShares;
        temp = Double.doubleToLongBits(valuePaid);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Position{" +
                "ticker='" + ticker + '\'' +
                ", numOfShares=" + numOfShares +
                ", valuePaid=" + valuePaid +
                '}';
    }
}
