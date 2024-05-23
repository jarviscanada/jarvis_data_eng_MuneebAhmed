package ca.jrvs.apps.jdbc.stockquote;

import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ca.jrvs.apps.stockquote.service.PositionService;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PositionService_UnitTest {
    @Mock
    private PositionDao positionDao;

    @InjectMocks
    private PositionService positionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void buy_NewTicker_CreatesPosition() {
        String ticker = "AAPL";
        int numberOfShares = 100;
        double price = 150.0;

        when(positionDao.findById(ticker)).thenReturn(Optional.empty());
        Position expectedPosition = new Position(ticker, numberOfShares, numberOfShares * price);
        when(positionDao.save(any(Position.class))).thenReturn(expectedPosition);

        Position result = positionService.buy(ticker, numberOfShares, price);

        assertNotNull(result);
        assertEquals(expectedPosition, result);
        verify(positionDao).save(any(Position.class));
    }

    @Test
    void sell_ExistingTicker_DeletesPosition() {
        String ticker = "AAPL";
        when(positionDao.findById(ticker)).thenReturn(Optional.of(new Position(ticker, 100, 15000.0)));

        positionService.sell(ticker);

        verify(positionDao).deleteById(ticker);
    }
}
