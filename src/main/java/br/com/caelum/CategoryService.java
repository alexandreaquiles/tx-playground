package br.com.caelum;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void failToInsertExistingCategoryInAnotherTxWithShutUpCatch() {
        try {
            failToInsertExistingCategory();
        } catch (PersistenceException ex) {
            // shutting up...
            logger.info("Exception {} shut up inside CategoryService. Message was: {}", ex.getClass().getSimpleName(), ex.getMessage());
        }
    }

    public void failToInsertExistingCategoryInAnotherTxWithShutUpCatchInAnotherMethod() {
        try {
            // Doesn't make sense
            failToInsertExistingCategoryInAnotherTx();
        } catch (PersistenceException ex) {
            // shutting up...
            logger.info("Exception {} shut up inside CategoryService. Message was: {}", ex.getClass().getSimpleName(), ex.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = PersistenceException.class)
    public  void failToInsertExistingCategoryInAnotherTxWithNoRollbackForPersistenceExceptionWithShutUpCatch() {
        try {
            failToInsertExistingCategory();
        } catch (PersistenceException ex) {
            // shutting up...
            logger.info("Exception {} shut up inside CategoryService. Message was: {}", ex.getClass().getSimpleName(), ex.getMessage());
        }
    }

    private void failToInsertExistingCategory() {
        entityManager.createNativeQuery("INSERT INTO Category (name) values (:name)")
                .setParameter("name", EXISTING_CATEGORY_NAME)
                .executeUpdate();
    }

}
