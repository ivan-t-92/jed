package provider.cbr;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class ValCursEntity {
    @Id
    LocalDate localDate;

    @Embedded
    ValCurs valCurs;

    public ValCursEntity() {}

    public ValCursEntity(LocalDate localDate, ValCurs valCurs) {
        this.localDate = localDate;
        this.valCurs = valCurs;
    }
}
