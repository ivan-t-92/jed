package servlet;

import servlet.exchange.CurrencyExchange;
import servlet.exchange.CurrencyExchangeJsonApi;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CurrencyExchangeRateServlet extends HttpServlet {

    private final CurrencyExchange currencyExchange = new CurrencyExchange();
    private final CurrencyExchangeJsonApi api = new CurrencyExchangeJsonApi(currencyExchange);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        writer.write(api.jsonResponse(req.getParameter("date"), req.getParameter("base")));
    }


}
