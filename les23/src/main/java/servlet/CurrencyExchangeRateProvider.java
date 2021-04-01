package servlet;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

public interface CurrencyExchangeRateProvider {

    Optional<Double> getExchangeRate(String currencyFrom, String currencyTo, LocalDate date);

    Stream<String> availableCurrencies(LocalDate date);
}
