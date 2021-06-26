package servlet.exchange.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class CurrenciesByDateEntity {
    @Id @NonNull
    private LocalDate localDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @NonNull
    private List<CurrencyEntity> currencies;
}
