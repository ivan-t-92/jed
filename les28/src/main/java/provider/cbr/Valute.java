package provider.cbr;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
class Valute {
    @Id
    @GeneratedValue
    private int persistenceId;

    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    private String id;

    @JacksonXmlProperty(localName = "NumCode")
    private int numCode;
    @JacksonXmlProperty(localName = "CharCode")
    private String charCode;
    @JacksonXmlProperty(localName = "Nominal")
    private int nominal;
    @JacksonXmlProperty(localName = "Name")
    private String name;
    @JacksonXmlProperty(localName = "Value")
    private String value;

    public double getValueAsDouble() {
        return Double.parseDouble(value.replace(',', '.'));
    }
}
