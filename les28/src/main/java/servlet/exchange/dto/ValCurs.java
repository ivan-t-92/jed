package servlet.exchange.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
public class ValCurs {
    @JacksonXmlProperty(localName = "Date", isAttribute = true)
    private String date;

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Valute")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Valute> valute = new ArrayList<>();
}
