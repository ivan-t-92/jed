package servlet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import servlet.exchange.CurrencyExchangeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyExchangeRateServletTest {

    private static final String DATE_PARAMETER_STR = "date";
    private static final String BASE_PARAMETER_STR = "base";
    private static final String DATE_PARAMETER_VALUE = "2021-06-27";
    private static final String BASE_PARAMETER_VALUE_RUB = "RUB";
    private static final String BASE_PARAMETER_VALUE_USD = "USD";

    @Mock
    private CurrencyExchangeService currencyExchangeService;
    @InjectMocks
    private CurrencyExchangeRateServlet servlet;

    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Mock
    private PrintWriter printWriter;

    @Test
    public void doGetGivenParameters() throws IOException {
        when(httpServletResponse.getWriter()).thenReturn(printWriter);
        when(httpServletRequest.getParameter(DATE_PARAMETER_STR)).thenReturn(DATE_PARAMETER_VALUE);
        when(httpServletRequest.getParameter(BASE_PARAMETER_STR)).thenReturn(BASE_PARAMETER_VALUE_USD);

        servlet.doGet(httpServletRequest, httpServletResponse);

        verify(currencyExchangeService).jsonResponse(LocalDate.parse(DATE_PARAMETER_VALUE), BASE_PARAMETER_VALUE_USD);
    }

    @Test
    public void doGetEmptyParameters() throws IOException {
        when(httpServletResponse.getWriter()).thenReturn(printWriter);
        when(httpServletRequest.getParameter(DATE_PARAMETER_STR)).thenReturn(null);
        when(httpServletRequest.getParameter(BASE_PARAMETER_STR)).thenReturn(null);

        servlet.doGet(httpServletRequest, httpServletResponse);

        verify(currencyExchangeService).jsonResponse(LocalDate.now(), BASE_PARAMETER_VALUE_RUB);
    }

    @Test
    public void doGetInvalidParameterDate() throws IOException {
        when(httpServletResponse.getWriter()).thenReturn(printWriter);
        when(httpServletRequest.getParameter(DATE_PARAMETER_STR)).thenReturn("");

        servlet.doGet(httpServletRequest, httpServletResponse);

        verify(currencyExchangeService, never()).jsonResponse(any(), any());
    }

    @Test
    public void doGetInvalidParameterBase() throws IOException {
        when(httpServletResponse.getWriter()).thenReturn(printWriter);
        when(httpServletRequest.getParameter(DATE_PARAMETER_STR)).thenReturn(DATE_PARAMETER_VALUE);
        String invalidBase = "123";
        when(httpServletRequest.getParameter(BASE_PARAMETER_STR)).thenReturn(invalidBase);

        servlet.doGet(httpServletRequest, httpServletResponse);

        verify(currencyExchangeService).jsonResponse(LocalDate.parse(DATE_PARAMETER_VALUE), invalidBase);
    }
}
