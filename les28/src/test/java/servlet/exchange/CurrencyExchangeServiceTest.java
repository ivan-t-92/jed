package servlet.exchange;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CurrencyExchangeServiceTest {

    private static final String USD_CODE = "USD";
    private static final String EUR_CODE = "EUR";
    private static final String RUB_CODE = "RUB";
    private static final double USD_RATE = 0.5;
    private static final double EUR_RATE = 0.25;

    private static LocalDate localDate;
    private static CurrenciesByDateEntity currenciesByDate;

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
        CbrExchangeRateProvider providerMock = createProviderMockWithData();
        CurrenciesByDatePersistence persistenceMock = createPersistenceMockWithData();
        CurrenciesByDateMapper mapperMock = createMapperMock();

        CurrencyExchangeService service = new CurrencyExchangeService(providerMock, persistenceMock, mapperMock);

        verifyResponse(service.jsonResponse(localDate, RUB_CODE));

        verify(persistenceMock).get(eq(localDate));
        verify(persistenceMock, never()).save(any());
        verify(providerMock, never()).load(any());
        verify(mapperMock).getCurrencies(eq(currenciesByDate), eq(RUB_CODE));
    }

    @Test
    void jsonResponseNoDataFromDb() {
        CbrExchangeRateProvider providerMock = createProviderMockWithData();
        CurrenciesByDatePersistence persistenceMock = createPersistenceMockWithoutData();
        CurrenciesByDateMapper mapperMock = createMapperMock();

        CurrencyExchangeService service = new CurrencyExchangeService(providerMock, persistenceMock, mapperMock);

        verifyResponse(service.jsonResponse(localDate, RUB_CODE));

        verify(persistenceMock).get(eq(localDate));
        verify(persistenceMock).save(eq(currenciesByDate));
        verify(providerMock).load(eq(localDate));
        verify(mapperMock).getCurrencies(eq(currenciesByDate), eq(RUB_CODE));
    }

    @Test
    void jsonResponseNoDataFromDbAndNoDataFromProvider() {
        CbrExchangeRateProvider providerMock = createProviderMockWithoutData();
        CurrenciesByDatePersistence persistenceMock = createPersistenceMockWithoutData();
        CurrenciesByDateMapper mapperMock = createMapperMock();

        CurrencyExchangeService service = new CurrencyExchangeService(providerMock, persistenceMock, mapperMock);
        service.jsonResponse(localDate, RUB_CODE);

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

    private CbrExchangeRateProvider createProviderMockWithData() {
        CbrExchangeRateProvider providerMock = mock(CbrExchangeRateProvider.class);
        when(providerMock.load(eq(localDate))).thenReturn(Optional.of(currenciesByDate));
        return providerMock;
    }

    private CbrExchangeRateProvider createProviderMockWithoutData() {
        CbrExchangeRateProvider providerMock = mock(CbrExchangeRateProvider.class);
        when(providerMock.load(any())).thenReturn(Optional.empty());
        return providerMock;
    }

    private CurrenciesByDatePersistence createPersistenceMockWithData() {
        CurrenciesByDatePersistence persistenceMock = mock(CurrenciesByDatePersistence.class);
        when(persistenceMock.get(eq(localDate))).thenReturn(Optional.of(currenciesByDate));
        return persistenceMock;
    }

    private CurrenciesByDatePersistence createPersistenceMockWithoutData() {
        CurrenciesByDatePersistence persistenceMock = mock(CurrenciesByDatePersistence.class);
        when(persistenceMock.get(any())).thenReturn(Optional.empty());
        return persistenceMock;
    }

    private CurrenciesByDateMapper createMapperMock() {
        CurrenciesByDateMapper mapperMock = mock(CurrenciesByDateMapper.class);
        when(mapperMock.getCurrencies(same(currenciesByDate), eq(RUB_CODE)))
                .thenReturn(Map.of(USD_CODE, USD_RATE, EUR_CODE, EUR_RATE));
        return mapperMock;
    }
}