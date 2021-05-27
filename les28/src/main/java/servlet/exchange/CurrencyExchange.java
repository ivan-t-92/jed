package servlet.exchange;

import servlet.exchange.dataaccess.persistence.ValCursPersistence;
import servlet.exchange.dataaccess.provider.CbrExchangeRateProvider;
import servlet.exchange.dto.ValCurs;
import servlet.exchange.dto.Valute;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

public class CurrencyExchange {

    private static final String RUB_CODE = "RUB";
    private final CbrExchangeRateProvider provider = new CbrExchangeRateProvider();
    private final ValCursPersistence persistence = new ValCursPersistence();


    public Stream<String> availableCurrencies(LocalDate date) {
        Optional<ValCurs> valCurs = load(date);
        if (valCurs.isPresent() && !valCurs.get().getValute().isEmpty()) {
            return Stream.concat(Stream.of(RUB_CODE), valCurs.get().getValute().stream().map(Valute::getCharCode));
        }
        return Stream.empty();
    }

    public Optional<Double> getExchangeRate(String currencyFrom, String currencyTo, LocalDate date) {
        return getExchangeRateRaw(currencyFrom, currencyTo, date).flatMap(rate -> Optional.of(roundRate(rate)));
    }

    private Optional<Double> getExchangeRateRaw(String currencyFrom, String currencyTo, LocalDate date) {
        if (currencyFrom.equals(RUB_CODE)) {
            return getRubExchangeRate(currencyTo, date);
        }
        if (currencyTo.equals(RUB_CODE)) {
            return getRubExchangeRate(currencyFrom, date).flatMap(rate -> Optional.of(1.0 / rate));
        }
        Optional<Double> rubRateFrom = getRubExchangeRate(currencyFrom, date);
        Optional<Double> rubRateTo = getRubExchangeRate(currencyTo, date);
        if (rubRateFrom.isPresent() && rubRateTo.isPresent()) {
            return Optional.of(rubRateTo.get() / rubRateFrom.get());
        }
        return Optional.empty();
    }

    private Optional<Double> getRubExchangeRate(String currency, LocalDate date) {
        Optional<ValCurs> valCurs = load(date);
        return valCurs.flatMap(curs -> curs.getValute().stream()
                .filter(valute -> valute.getCharCode().equals(currency))
                .findFirst()
                .map(valute -> 1.0 / (valute.getValueAsDouble() / valute.getNominal())));
    }

    private static double roundRate(double value) {
        double mul = 1000000.0;
        return Math.round(value * mul) / mul;
    }

    private Optional<ValCurs> load(LocalDate date) {
        Optional<ValCurs> curs = persistence.get(date);
        if (curs.isEmpty()) {
            curs = provider.load(date);
            curs.ifPresent(valCurs -> persistence.save(date, valCurs));
        }
        return curs;
    }
}
