package servlet;

import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

@Data
class ResponseData {
    private String date;
    private String base;
    private Map<String, Double> rates = new TreeMap<>();
}
