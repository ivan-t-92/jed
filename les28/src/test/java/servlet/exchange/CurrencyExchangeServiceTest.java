package servlet.exchange;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import servlet.exchange.dataaccess.persistence.CurrenciesByDatePersistence;
import servlet.exchange.dataaccess.provider.CbrExchangeRateProvider;
import servlet.exchange.entity.CurrenciesByDateEntity;
import servlet.exchange.entity.CurrencyEntity;
import servlet.exchange.mapper.CurrenciesByDateMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeServiceTest {

    private static final String USD_CODE = "USD";
    private static final String EUR_CODE = "EUR";
    private static final String RUB_CODE = "RUB";
    private static final double USD_RATE = 0.5;
    private static final double EUR_RATE = 0.25;

    private static LocalDate localDate;
    private static CurrenciesByDateEntity currenciesByDate;

    @Mock
    private CbrExchangeRateProvider providerMock;
    @Mock
    private CurrenciesByDatePersistence persistenceMock;
    @Mock
    private CurrenciesByDateMapper mapperMock;
    @InjectMocks
    private CurrencyExchangeService service;

    @BeforeAll
    static void init() {
        localDate = LocalDate.now();
        List<CurrencyEntity> currencies = new ArrayList<>();
        currencies.add(new CurrencyEntity(USD_CODE, USD_RATE));
        currencies.add(new CurrencyEntity(EUR_CODE, EUR_RATE));
        currenciesByDate = new CurrenciesByDateEntity(localDate, currencies);
    }

    @Test
    void jsonResponseHasDataInDb() {
        when(persistenceMock.get(localDate)).thenReturn(Optional.of(currenciesByDate));
        when(mapperMock.getCurrencies(currenciesByDate,RUB_CODE))
                .thenReturn(Map.of(USD_CODE, USD_RATE, EUR_CODE, EUR_RATE));

        verifyResponse(service.jsonResponse(localDate, RUB_CODE));

        verify(persistenceMock).get(eq(localDate));
        verify(persistenceMock, never()).save(any());
        verify(providerMock, never()).load(any());
        verify(mapperMock).getCurrencies(eq(currenciesByDate), eq(RUB_CODE));
    }

    @Test
    void jsonResponseNoDataFromDb() {
        when(providerMock.load(localDate)).thenReturn(Optional.of(currenciesByDate));
        when(persistenceMock.get(any())).thenReturn(Optional.empty());
        when(mapperMock.getCurrencies(currenciesByDate, RUB_CODE))
                .thenReturn(Map.of(USD_CODE, USD_RATE, EUR_CODE, EUR_RATE));

        verifyResponse(service.jsonResponse(localDate, RUB_CODE));

        verify(persistenceMock).get(eq(localDate));
        verify(persistenceMock).save(eq(currenciesByDate));
        verify(providerMock).load(eq(localDate));
        verify(mapperMock).getCurrencies(eq(currenciesByDate), eq(RUB_CODE));
    }

    @Test
    void jsonResponseNoDataFromDbAndNoDataFromProvider() {
        when(providerMock.load(any())).thenReturn(Optional.empty());
        when(persistenceMock.get(any())).thenReturn(Optional.empty());

        verifyEmptyResponse(service.jsonResponse(localDate, RUB_CODE));

        verify(persistenceMock).get(eq(localDate));
        verify(persistenceMock, never()).save(any());
        verify(providerMock).load(eq(localDate));
        verify(mapperMock, never()).getCurrencies(any(), any());
    }

    void verifyResponse(ResponseData responseData) {
        assertEquals(localDate, responseData.getDate());
        assertEquals(RUB_CODE, responseData.getBase());
        assertEquals(2, responseData.getRates().size());
        assertEquals(USD_RATE, responseData.getRates().get(USD_CODE));
        assertEquals(EUR_RATE, responseData.getRates().get(EUR_CODE));
    }

    void verifyEmptyResponse(ResponseData responseData) {
        assertEquals(localDate, responseData.getDate());
        assertEquals(RUB_CODE, responseData.getBase());
        assertTrue(responseData.getRates().isEmpty());
    }
}