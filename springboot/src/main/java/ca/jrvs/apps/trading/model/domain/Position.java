package ca.jrvs.apps.trading.model.domain;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Immutable
@Table(name = "position", schema = "public")
@Subselect("select ps.*, count(*) over() as id from position ps")
@IdClass(Position.PositionId.class)
public class Position {

    @Id
    @Column(name = "account_id")
    private Integer accountId;

    @Id
    @Column(name = "ticker")
    private String ticker;

    @Column(name = "position")
    private Long position;

    // Getters and Setters

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public static class PositionId implements Serializable {
        private Integer accountId;
        private String ticker;

        public PositionId() {
        }

        public PositionId(Integer accountId, String ticker) {
            this.accountId = accountId;
            this.ticker = ticker;
        }

        // Getters and Setters
        public Integer getAccountId() {
            return accountId;
        }

        public void setAccountId(Integer accountId) {
            this.accountId = accountId;
        }

        public String getTicker() {
            return ticker;
        }

        public void setTicker(String ticker) {
            this.ticker = ticker;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PositionId that = (PositionId) o;
            return Objects.equals(accountId, that.accountId) &&
                    Objects.equals(ticker, that.ticker);
        }

        @Override
        public int hashCode() {
            return Objects.hash(accountId, ticker);
        }
    }
}
