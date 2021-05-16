package servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import provider.CurrencyExchangeRateProvider;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CurrencyExchangeRateServlet extends HttpServlet {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final CurrencyExchangeRateProvider provider;

    public CurrencyExchangeRateServlet(CurrencyExchangeRateProvider provider) {
        this.provider = provider;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String dateStr = req.getParameter("date");
        LocalDate localDate;
        if (dateStr != null) {
            try {
                localDate = LocalDate.parse(dateStr);
            } catch (DateTimeParseException e) {
                PrintWriter writer = resp.getWriter();
                writer.write("error: invalid date");
                return;
            }
        } else {
            localDate = LocalDate.now();
        }

        String baseCurrency = req.getParameter("base");
        if (baseCurrency == null) {
            baseCurrency = "RUB";
        }

        PrintWriter writer = resp.getWriter();
        writer.write(jsonResponse(localDate, baseCurrency));
    }

    private String jsonResponse(LocalDate localDate, String baseCurrency) throws JsonProcessingException {
        ResponseData data = new ResponseData();
        data.setBase(baseCurrency);
        data.setDate(localDate.toString());

        if (provider.availableCurrencies(localDate).anyMatch(s -> s.equals(baseCurrency))) {
            provider.availableCurrencies(localDate)
                    .filter(s -> !s.equals(baseCurrency))
                    .forEach(s -> data.getRates().put(s, provider.getExchangeRate(baseCurrency, s, localDate).get()));
        }
        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(data);
    }
}
