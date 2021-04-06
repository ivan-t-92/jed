package provider.cbr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import provider.CurrencyExchangeRateProvider;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Optional;
import java.util.stream.Stream;

import static java.time.temporal.ChronoField.*;

public class CbrExchangeRateProvider implements CurrencyExchangeRateProvider {

    private static final String BASE_URL = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=";
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(DAY_OF_MONTH, 2)
            .appendLiteral('/')
            .appendValue(MONTH_OF_YEAR, 2)
            .appendLiteral('/')
            .appendValue(YEAR, 4)
            .toFormatter();
    private static final String RUB_CODE = "RUB";
    private static final int CACHE_SIZE = 1024;
    private final Cache<LocalDate, ValCurs> cache = new Cache<>(CACHE_SIZE);

    @Override
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

    private double roundRate(double value) {
        double mul = 1000000.0;
        return Math.round(value * mul) / mul;
    }

    @Override
    public Stream<String> availableCurrencies(LocalDate date) {
        Optional<ValCurs> valCurs = load(date);
        if (valCurs.isPresent() && !valCurs.get().getValute().isEmpty()) {
            return Stream.concat(Stream.of(RUB_CODE), valCurs.get().getValute().stream().map(Valute::getCharCode));
        }
        return Stream.empty();
    }

    private Optional<ValCurs> load(LocalDate date) {
        Optional<ValCurs> curs;
        if ((curs = cache.get(date)).isPresent()) {
            return curs;
        }
        String url = BASE_URL + date.format(FORMATTER);
        try (InputStream is = new URL(url).openStream()) {
            byte[] xmlBytes = is.readAllBytes();
            String xmlStr = new String(xmlBytes);
            XmlMapper xmlMapper = new XmlMapper();
            try {
                ValCurs valCurs = xmlMapper.readValue(xmlStr, ValCurs.class);
                cache.put(date, valCurs);
                return Optional.of(valCurs);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
