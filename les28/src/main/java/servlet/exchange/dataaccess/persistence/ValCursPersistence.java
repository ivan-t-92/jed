package servlet.exchange.dataaccess.persistence;

import servlet.exchange.dto.ValCurs;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.Optional;

public class ValCursPersistence {

    private final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("ValCursTable");

    public Optional<ValCurs> get(LocalDate date) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Optional<ValCurs> result =
                Optional.ofNullable(entityManager.find(ValCursEntity.class, date))
                        .map(e -> e.valCurs);
        entityManager.close();
        return result;
    }

    public void save(LocalDate date, ValCurs valCurs) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(new ValCursEntity(date, valCurs));
        transaction.commit();
        entityManager.close();
    }
}
