package servlet.exchange.mapper;

import servlet.exchange.entity.CurrenciesByDateEntity;
import servlet.exchange.entity.CurrencyEntity;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CurrenciesByDateMapper {

    public Map<String, Double> getCurrencies(CurrenciesByDateEntity currenciesByDate, String baseCurrencyStr) {
        Optional<CurrencyEntity> baseCurrency = currenciesByDate.getCurrencies().stream()
                .filter(currency -> currency.getCharCode().equals(baseCurrencyStr))
                .findFirst();
        return baseCurrency.map(
                value -> Stream.concat(currenciesByDate.getCurrencies().stream(), Stream.of(new CurrencyEntity("RUB", 1.0)))
                        .filter(currency -> !currency.getCharCode().equals(baseCurrencyStr))
                        .collect(Collectors.toMap(CurrencyEntity::getCharCode, currency -> getExchangeRate(value, currency))))
                .orElseGet(() -> currenciesByDate.getCurrencies().stream()
                        .collect(Collectors.toMap(CurrencyEntity::getCharCode, c -> 1.0 / c.getRateToRub())));
    }

    private Double getExchangeRate(CurrencyEntity baseCurrency, CurrencyEntity currentCurrency) {
        return baseCurrency.getRateToRub() / currentCurrency.getRateToRub();
    }
}
