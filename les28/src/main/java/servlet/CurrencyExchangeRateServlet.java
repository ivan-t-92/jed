package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import servlet.exchange.CurrencyExchangeService;
import servlet.exchange.ResponseData;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@RequiredArgsConstructor
public class CurrencyExchangeRateServlet extends HttpServlet {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    private final CurrencyExchangeService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        String dateStr = req.getParameter("date");
        LocalDate localDate;
        if (dateStr != null) {
            try {
                localDate = LocalDate.parse(dateStr);
            } catch (DateTimeParseException e) {
                writer.write("error: invalid date");
                return;
            }
        } else {
            localDate = LocalDate.now();
        }
        String currency = Optional.ofNullable(req.getParameter("base"))
                .orElse("RUB");
        writer.write(serializeResponse(service.jsonResponse(localDate, currency)));
    }


    @SneakyThrows
    private String serializeResponse(ResponseData responseData) {
        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(responseData);
    }


}
