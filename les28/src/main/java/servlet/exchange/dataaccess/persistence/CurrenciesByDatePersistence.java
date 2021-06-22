package servlet.exchange.dataaccess.persistence;

import servlet.exchange.entity.CurrenciesByDateEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.Optional;

public class CurrenciesByDatePersistence {

    private final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("ExchangeRateTable");

    public Optional<CurrenciesByDateEntity> get(LocalDate date) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Optional<CurrenciesByDateEntity> result =
                Optional.ofNullable(entityManager.find(CurrenciesByDateEntity.class, date));
        entityManager.close();
        return result;
    }

    public void save(CurrenciesByDateEntity entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(entity);
        transaction.commit();
        entityManager.close();
    }
}
