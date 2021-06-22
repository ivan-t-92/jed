package servlet.exchange;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

@Data
@Builder
public class ResponseData {
    private LocalDate date;
    private String base;
    private Map<String, Double> rates;
}
