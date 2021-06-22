import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import servlet.CurrencyExchangeRateServlet;
import servlet.exchange.CurrencyExchangeService;
import servlet.exchange.dataaccess.persistence.CurrenciesByDatePersistence;
import servlet.exchange.dataaccess.provider.CbrExchangeRateProvider;
import servlet.exchange.mapper.CurrenciesByDateMapper;

import java.io.File;

public class Main {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        Context context = tomcat.addContext("", new File(".").getAbsolutePath());


        String servletName = "ExchangeRatesServlet";
        Tomcat.addServlet(context, servletName,
                new CurrencyExchangeRateServlet(
                        new CurrencyExchangeService(
                                new CbrExchangeRateProvider(),
                                new CurrenciesByDatePersistence(),
                                new CurrenciesByDateMapper())));
        context.addServletMappingDecoded("/exchange-rates/api", servletName);

        tomcat.start();
        tomcat.getServer().await();
    }
}
