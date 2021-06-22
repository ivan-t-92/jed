package servlet.exchange.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class CurrencyEntity {
    @Id @GeneratedValue
    int id;

    @NonNull
    String charCode;

    @NonNull
    Double rateToRub;
}
