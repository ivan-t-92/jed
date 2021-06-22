package servlet.exchange.dataaccess.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import servlet.exchange.dto.CbrCurrency;
import servlet.exchange.dto.CbrExchangeRates;
import servlet.exchange.entity.CurrenciesByDateEntity;
import servlet.exchange.entity.CurrencyEntity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoField.*;

public class CbrExchangeRateProvider {

    private static final String BASE_URL = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=";
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(DAY_OF_MONTH, 2)
            .appendLiteral('/')
            .appendValue(MONTH_OF_YEAR, 2)
            .appendLiteral('/')
            .appendValue(YEAR, 4)
            .toFormatter();


    public Optional<CurrenciesByDateEntity> load(LocalDate date) {
        String url = BASE_URL + date.format(FORMATTER);
        try (InputStream is = new URL(url).openStream()) {
            byte[] xmlBytes = is.readAllBytes();
            String xmlStr = new String(xmlBytes);
            XmlMapper xmlMapper = new XmlMapper();
            try {
                CbrExchangeRates exchangeRates = xmlMapper.readValue(xmlStr, CbrExchangeRates.class);
                return Optional.of(mapCurrenciesByDateToEntity(exchangeRates, date));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private CurrenciesByDateEntity mapCurrenciesByDateToEntity(CbrExchangeRates exchangeRates, LocalDate date) {
        List<CurrencyEntity> currencies = new ArrayList<>();
        exchangeRates.getCurrencies().forEach(cbrCurrency -> currencies.add(mapCurrencyToEntity(cbrCurrency)));
        return new CurrenciesByDateEntity(date, currencies);
    }

    private CurrencyEntity mapCurrencyToEntity(CbrCurrency cbrCurrency) {
        double rate = Double.parseDouble(cbrCurrency.getValue().replace(',', '.')) / cbrCurrency.getNominal();
        return new CurrencyEntity(cbrCurrency.getCharCode(), rate);
    }
}
