package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteDao extends JpaRepository<Quote, Long> {
}
