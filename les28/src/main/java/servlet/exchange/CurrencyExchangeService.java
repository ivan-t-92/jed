package servlet.exchange;

import lombok.RequiredArgsConstructor;
import servlet.exchange.dataaccess.persistence.CurrenciesByDatePersistence;
import servlet.exchange.dataaccess.provider.CbrExchangeRateProvider;
import servlet.exchange.entity.CurrenciesByDateEntity;
import servlet.exchange.mapper.CurrenciesByDateMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
public class CurrencyExchangeService {

    private final CbrExchangeRateProvider provider;
    private final CurrenciesByDatePersistence persistence;
    private final CurrenciesByDateMapper mapper;

    public ResponseData jsonResponse(LocalDate date, String baseCurrency) {
        return ResponseData.builder()
                .base(baseCurrency)
                .date(date)
                .rates(getCurrenciesByDate(date)
                        .map(currenciesByDateEntity -> mapper.getCurrencies(currenciesByDateEntity, baseCurrency))
                        .orElse(Collections.emptyMap()))
                .build();
    }

    private Optional<CurrenciesByDateEntity> getCurrenciesByDate(LocalDate date) {
        Optional<CurrenciesByDateEntity> curs = persistence.get(date);
        if (curs.isEmpty()) {
            curs = provider.load(date);
            curs.ifPresent(persistence::save);
        }
        return curs;
    }

}
