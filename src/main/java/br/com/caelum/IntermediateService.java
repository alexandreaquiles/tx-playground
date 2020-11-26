package br.com.caelum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class IntermediateService {

    private static final Logger logger = LoggerFactory.getLogger(IntermediateService.class);

    private final CategoryRepository categoryRepository;

    public IntermediateService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void failToInsertExistingCategoryInAnotherTxWithShutUpCatch() {
        try {
            categoryRepository.failToInsertExistingCategoryInAnotherTx();
        } catch (DataIntegrityViolationException ex) {
            // shutting up...
            logger.info("Exception {} shut up. Message was: {}", ex.getClass().getSimpleName(), ex.getMessage());
        }
    }

    public void failToInsertExistingCategoryWithSameTxWithShutUpCatch() {
        try {
            categoryRepository.failToInsertExistingCategoryWithSameTx();
        } catch (DataIntegrityViolationException ex) {
            // shutting up...
            logger.info("Exception {} shut up. Message was: {}", ex.getClass().getSimpleName(), ex.getMessage());
        }
    }

}
