package br.com.caelum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;

@Service
public class IntermediateService {

    private static final Logger logger = LoggerFactory.getLogger(IntermediateService.class);

    private final CategoryService categoryService;

    public IntermediateService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void failToInsertExistingCategoryInAnotherTxWithShutUpCatch() {
        try {
            categoryService.failToInsertExistingCategoryInAnotherTx();
        } catch (PersistenceException ex) {
            // shutting up...
            logger.info("Exception {} shut up. Message was: {}", ex.getClass().getSimpleName(), ex.getMessage());
        }
    }

    public void failToInsertExistingCategoryWithSameTxWithShutUpCatch() {
        try {
            categoryService.failToInsertExistingCategoryWithSameTx();
        } catch (PersistenceException ex) {
            // shutting up...
            logger.info("Exception {} shut up. Message was: {}", ex.getClass().getSimpleName(), ex.getMessage());
        }
    }

}
