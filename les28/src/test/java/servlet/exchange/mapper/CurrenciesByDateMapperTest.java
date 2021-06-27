package servlet.exchange.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import servlet.exchange.entity.CurrenciesByDateEntity;
import servlet.exchange.entity.CurrencyEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CurrenciesByDateMapperTest {

    private static final String USD_CODE = "USD";
    private static final String EUR_CODE = "EUR";
    private static final String RUB_CODE = "RUB";
    private static final double USD_RATE = 0.5;
    private static final double EUR_RATE = 0.25;

    private static CurrenciesByDateEntity currenciesByDate;

    @BeforeAll
    static void init() {
        List<CurrencyEntity> currencies = new ArrayList<>();
        currencies.add(new CurrencyEntity(USD_CODE, USD_RATE));
        currencies.add(new CurrencyEntity(EUR_CODE, EUR_RATE));
        currenciesByDate = new CurrenciesByDateEntity(LocalDate.now(), currencies);
    }

    @Test
    public void getCurrenciesRubBase() {
        Map<String, Double> currencies =  new CurrenciesByDateMapper().getCurrencies(currenciesByDate, RUB_CODE);
        verifyRubBase(currencies);
    }

    @Test
    public void getCurrenciesEmptyBase() {
        Map<String, Double> currencies =  new CurrenciesByDateMapper().getCurrencies(currenciesByDate, "");
        verifyRubBase(currencies);
    }

    private void verifyRubBase(Map<String, Double> currencies) {
        assertEquals(2, currencies.size());
        assertEquals(1.0 / USD_RATE, currencies.get(USD_CODE));
        assertEquals(1.0 / EUR_RATE, currencies.get(EUR_CODE));
    }

    @Test
    public void getCurrenciesUsdBase() {
        Map<String, Double> currencies =  new CurrenciesByDateMapper().getCurrencies(currenciesByDate, USD_CODE);

        assertEquals(2, currencies.size());
        assertEquals(USD_RATE, currencies.get(RUB_CODE));
        assertEquals(USD_RATE / EUR_RATE, currencies.get(EUR_CODE));
    }

    @Test
    public void getCurrenciesEurBase() {
        Map<String, Double> currencies =  new CurrenciesByDateMapper().getCurrencies(currenciesByDate, EUR_CODE);

        assertEquals(2, currencies.size());
        assertEquals(EUR_RATE, currencies.get(RUB_CODE));
        assertEquals(EUR_RATE / USD_RATE, currencies.get(USD_CODE));
    }
}
