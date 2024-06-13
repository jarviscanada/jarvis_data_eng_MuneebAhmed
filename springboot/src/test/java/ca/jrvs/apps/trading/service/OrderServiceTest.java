package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.*;
import ca.jrvs.apps.trading.model.domain.*;
import ca.jrvs.apps.trading.model.domain.MarketOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Captor
    ArgumentCaptor<SecurityOrder> captorSecurityOrder;

    @Mock
    private AccountDao accountDao;
    @Mock
    private SecurityOrderDao securityOrderDao;
    @Mock
    private QuoteDao quoteDao;
    @Mock
    private PositionDao positionDao;

    @InjectMocks
    private OrderService orderService;

    private MarketOrder marketOrder;
    private Account account;
    private Quote quote;
    private SecurityOrder securityOrder;

    @Before
    public void setup() {
        marketOrder = new MarketOrder();
        marketOrder.setTicker("AAPL");
        marketOrder.setSize(10);
        marketOrder.setTraderId(1);
        marketOrder.setOption(MarketOrder.Option.BUY);

        account = new Account();
        account.setId(1);
        account.setTraderId(1);
        account.setAmount(1000.0);

        quote = new Quote();
        quote.setTicker("AAPL");
        quote.setLastPrice(150.0);
        quote.setAskPrice(151.0);
        quote.setBidPrice(149.0);
        quote.setAskSize(100);
        quote.setBidSize(100);

        securityOrder = new SecurityOrder();
    }

    @Test
    public void executeMarketOrder_buyOrder_success() {
        account.setAmount(2000.0);

        when(accountDao.findById(anyInt())).thenReturn(Optional.of(account));
        when(quoteDao.findById(anyString())).thenReturn(Optional.of(quote));
        when(securityOrderDao.save(any())).thenReturn(securityOrder);

        SecurityOrder result = orderService.executeMarketOrder(marketOrder);

        assertNotNull(result);
        verify(securityOrderDao).save(captorSecurityOrder.capture());
        assertEquals("FILLED", captorSecurityOrder.getValue().getStatus());
        assertEquals(Integer.valueOf(10), captorSecurityOrder.getValue().getSize());
        assertEquals(Double.valueOf(151.0), captorSecurityOrder.getValue().getPrice());
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeMarketOrder_invalidSize() {
        marketOrder.setSize(0);
        orderService.executeMarketOrder(marketOrder);
    }

    @Test
    public void executeMarketOrder_insufficientFunds() {
        account.setAmount(500.0);
        when(accountDao.findById(anyInt())).thenReturn(Optional.of(account));
        when(quoteDao.findById(anyString())).thenReturn(Optional.of(quote));

        SecurityOrder result = orderService.executeMarketOrder(marketOrder);


        verify(securityOrderDao).save(captorSecurityOrder.capture());
        assertEquals("CANCELED", captorSecurityOrder.getValue().getStatus());
        assertEquals("Insufficient funds", captorSecurityOrder.getValue().getNotes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeMarketOrder_invalidTicker() {
        when(accountDao.findById(anyInt())).thenReturn(Optional.of(account));
        when(quoteDao.findById(anyString())).thenReturn(Optional.empty());

        orderService.executeMarketOrder(marketOrder);
    }

    @Test
    public void executeMarketOrder_sellOrder_success() {
        marketOrder.setOption(MarketOrder.Option.SELL);

        when(accountDao.findById(anyInt())).thenReturn(Optional.of(account));
        when(quoteDao.findById(anyString())).thenReturn(Optional.of(quote));

        Position position = new Position();
        position.setAccountId(account.getId());
        position.setTicker(marketOrder.getTicker());
        position.setPosition(20L);  // Ensure there is enough position to sell
        when(positionDao.findById(any())).thenReturn(Optional.of(position));

        when(securityOrderDao.save(any())).thenReturn(securityOrder);

        SecurityOrder result = orderService.executeMarketOrder(marketOrder);

        assertNotNull(result);
        verify(securityOrderDao).save(captorSecurityOrder.capture());
        assertEquals("FILLED", captorSecurityOrder.getValue().getStatus());
        assertEquals(Integer.valueOf(10), captorSecurityOrder.getValue().getSize());
        assertEquals(Double.valueOf(149.0), captorSecurityOrder.getValue().getPrice());
    }

    @Test
    public void executeMarketOrder_noPosition() {
        marketOrder.setOption(MarketOrder.Option.SELL);
        when(accountDao.findById(anyInt())).thenReturn(Optional.of(account));
        when(quoteDao.findById(anyString())).thenReturn(Optional.of(quote));
        when(positionDao.findById(any())).thenReturn(Optional.empty());

        SecurityOrder result = orderService.executeMarketOrder(marketOrder);

        verify(securityOrderDao).save(captorSecurityOrder.capture());
        assertEquals("CANCELED", captorSecurityOrder.getValue().getStatus());
        assertEquals("Insufficient position", captorSecurityOrder.getValue().getNotes());
    }
}