package servlet.exchange.dataaccess.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import servlet.exchange.dto.ValCurs;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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


    public Optional<ValCurs> load(LocalDate date) {
        String url = BASE_URL + date.format(FORMATTER);
        try (InputStream is = new URL(url).openStream()) {
            byte[] xmlBytes = is.readAllBytes();
            String xmlStr = new String(xmlBytes);
            XmlMapper xmlMapper = new XmlMapper();
            try {
                ValCurs valCurs = xmlMapper.readValue(xmlStr, ValCurs.class);
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
