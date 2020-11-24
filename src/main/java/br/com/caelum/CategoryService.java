package br.com.caelum;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Service
public class CategoryService {

    public static final String EXISTING_CATEGORY_NAME = "Programming";

    private final EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;

    public CategoryService(EntityManager entityManager, TransactionTemplate transactionTemplate) {
        this.entityManager = entityManager;
        this.transactionTemplate = transactionTemplate;
    }

    @PostConstruct
    void saveExistingCategory() {
        transactionTemplate.executeWithoutResult(txStatus -> {
            entityManager.persist(new Category(EXISTING_CATEGORY_NAME));
        });
    }

    @Transactional
    public void failToInsertExistingCategoryWithSameTx() {
        failToInsertExistingCategory();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void failToInsertExistingCategoryInAnotherTx() {
        failToInsertExistingCategory();
    }

    private void failToInsertExistingCategory() {
        entityManager.createNativeQuery("INSERT INTO Category (name) values (:name)")
                .setParameter("name", EXISTING_CATEGORY_NAME)
                .executeUpdate();
    }

}
