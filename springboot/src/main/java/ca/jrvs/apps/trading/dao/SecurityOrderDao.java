package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SecurityOrderDao extends JpaRepository<SecurityOrder, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM SecurityOrder s WHERE s.account.id = ?1")
    void deleteByAccountId(@Param("accountId") Integer accountId);
}
