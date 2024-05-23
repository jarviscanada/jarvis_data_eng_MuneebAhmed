package ca.jrvs.apps.jdbc.stockquote;

import ca.jrvs.apps.stockquote.Quote;
import ca.jrvs.apps.stockquote.QuoteHttpHelper;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class QuoteHttpHelperTest {
    @Mock
    private OkHttpClient mockClient;

    private QuoteHttpHelper quoteHelper;
    private final String apiKey = "6BIUVEQ38AG801L4";

    @BeforeEach
    void setUp() {
        quoteHelper = new QuoteHttpHelper(apiKey);
        quoteHelper.setClient(mockClient);
    }

    @Test
    void testFetchQuoteInfo() throws Exception {
        String symbol = "MSFT";
        String jsonResponse = "{\"Global Quote\": {\"01. symbol\": \"" + symbol + "\", \"05. price\": \"280.00\"}}";

        Request expectedRequest = new Request.Builder()
                .url("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&datatype=json&apikey=" + apiKey)
                .build();

        Response mockResponse = new Response.Builder()
                .request(expectedRequest)
                .protocol(Protocol.HTTP_1_1)
                .code(200) // HTTP OK
                .message("OK")
                .body(ResponseBody.create(jsonResponse, MediaType.get("application/json")))
                .build();

        Call mockCall = mock(Call.class);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

        Quote result = quoteHelper.fetchQuoteInfo(symbol);

        assertNotNull(result);
        assertEquals(symbol, result.getSymbol());
        assertEquals(280.00, result.getPrice());
    }
}
