package servlet.exchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.Nullable;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CurrencyExchangeJsonApi {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final CurrencyExchange currencyExchange;

    public CurrencyExchangeJsonApi(CurrencyExchange currencyExchange) {
        this.currencyExchange = currencyExchange;
    }

    public String jsonResponse(@Nullable String dateStr, @Nullable String baseCurrencyStr) throws JsonProcessingException {
        LocalDate localDate;
        if (dateStr != null) {
            try {
                localDate = LocalDate.parse(dateStr);
            } catch (DateTimeParseException e) {
                return "error: invalid date";
            }
        } else {
            localDate = LocalDate.now();
        }
        final String baseCurrency = baseCurrencyStr != null ? baseCurrencyStr : "RUB";

        ResponseData data = new ResponseData();
        data.setBase(baseCurrency);
        data.setDate(localDate.toString());

        if (currencyExchange.availableCurrencies(localDate).anyMatch(s -> s.equals(baseCurrency))) {
            currencyExchange.availableCurrencies(localDate)
                    .filter(s -> !s.equals(baseCurrency))
                    .forEach(s -> data.getRates().put(s, currencyExchange.getExchangeRate(baseCurrency, s, localDate).get()));
        }
        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(data);
    }

}
