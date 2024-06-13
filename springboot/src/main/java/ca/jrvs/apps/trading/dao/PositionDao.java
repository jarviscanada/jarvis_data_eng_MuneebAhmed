package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionDao extends JpaRepository<Position, Position.PositionId> {
    long countByAccountId(Integer accountId);
    List<Position> findByAccountId(Integer accountId);
}

