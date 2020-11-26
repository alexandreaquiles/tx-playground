package br.com.caelum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

@RestController
public class CoursesController {

    private static final Logger logger = LoggerFactory.getLogger(CoursesController.class);

    private final EntityManager entityManager;
    private final CategoryRepository categoryRepository;
    private final IntermediateService intermediateService;

    public CoursesController(EntityManager entityManager, CategoryRepository categoryRepository, IntermediateService intermediateService) {
        this.entityManager = entityManager;
        this.categoryRepository = categoryRepository;
        this.intermediateService = intermediateService;
    }

    @Transactional
    @GetMapping("/same-tx-without-shut-up-catch")
    public ResponseEntity<Course> sameTxWithoutShutUpCatch() {
        Course newCourse = new Course("Java", 40);
        Assert.isNull(newCourse.getId(), "Can't add a course that already has an id.");
        entityManager.persist(newCourse);
        categoryRepository.failToInsertExistingCategoryWithSameTx();
        return ResponseEntity.ok(newCourse);
    }

    @Transactional
    @GetMapping("/same-tx-with-shut-up-catch")
    public ResponseEntity<Course> sameTxWithShutUpCatch() {
        Course newCourse = new Course("Python", 40);
        entityManager.persist(newCourse);
        try {
            categoryRepository.failToInsertExistingCategoryWithSameTx();
        } catch (DataIntegrityViolationException ex) {
            // shutting up...
            logger.info("Exception {} shut up. Message was: {}", ex.getClass().getSimpleName(), ex.getMessage());
        }
        return ResponseEntity.ok(newCourse);
    }

    @Transactional
    @GetMapping("/another-tx-with-shut-up-catch")
    public ResponseEntity<Course> anotherTxWithShutUpCatch() {
        Course newCourse = new Course("Go", 40);
        entityManager.persist(newCourse);
        try {
            categoryRepository.failToInsertExistingCategoryInAnotherTx();
        } catch (DataIntegrityViolationException ex) {
            // shutting up...
            logger.info("Exception {} shut up. Message was: {}", ex.getClass().getSimpleName(), ex.getMessage());
        }
        return ResponseEntity.ok(newCourse);
    }

    @Transactional
    @GetMapping("/another-tx-with-shut-up-catch-inside")
    public ResponseEntity<Course> anotherTxWithShutUpCatchInside() {
        Course newCourse = new Course("Haskell", 160);
        entityManager.persist(newCourse);
        categoryRepository.failToInsertExistingCategoryInAnotherTxWithShutUpCatch();
        return ResponseEntity.ok(newCourse);
    }

    @Transactional
    @GetMapping("/another-tx-with-shut-up-catch-inside-but-in-another-method")
    public ResponseEntity<Course> anotherTxWithShutUpCatchInsideButInAnotherMethod() {
        Course newCourse = new Course("Lisp", 2600);
        entityManager.persist(newCourse);
        categoryRepository.failToInsertExistingCategoryInAnotherTxWithShutUpCatchInAnotherMethod();
        return ResponseEntity.ok(newCourse);
    }

    @Transactional
    @GetMapping("/another-tx-with-shut-up-catch-and-norollback-for-exception-inside")
    public ResponseEntity<Course> anotherTxWithShutUpCatchInsideWithNoRollbackForException() {
        Course newCourse = new Course("JavaScript", 2);
        entityManager.persist(newCourse);
        categoryRepository.failToInsertExistingCategoryInAnotherTxWithNoRollbackForPersistenceAndWithShutUpCatch();
        return ResponseEntity.ok(newCourse);
    }

    @Transactional(noRollbackFor = DataIntegrityViolationException.class)
    @GetMapping("/another-tx-with-shut-up-catch-inside-but-with-norollback-for-exception-outside")
    public ResponseEntity<Course> anotherTxWithShutUpCatchInsideButWithNoRollbackForExceptionOutside() {
        Course newCourse = new Course("Rust", 3200);
        entityManager.persist(newCourse);
        categoryRepository.failToInsertExistingCategoryInAnotherTxWithShutUpCatch();
        return ResponseEntity.ok(newCourse);
    }

    @Transactional(noRollbackFor = DataIntegrityViolationException.class)
    @GetMapping("/another-tx-with-shut-up-catch-and-norollback-for-exception-outside")
    public ResponseEntity<Course> anotherTxWithShutUpCatchAndNoRollbackForExceptionOutside() {
        Course newCourse = new Course("Fortran", 80);
        entityManager.persist(newCourse);
        try {
            categoryRepository.failToInsertExistingCategoryInAnotherTx();
        } catch (DataIntegrityViolationException ex) {
            // shutting up...
            logger.info("Exception {} shut up. Message was: {}", ex.getClass().getSimpleName(), ex.getMessage());
        }
        return ResponseEntity.ok(newCourse);
    }

    @Transactional
    @GetMapping("/another-tx-with-shut-up-catch-in-intermediate-service")
    public ResponseEntity<Course> anotherTxWithShutUpCatchInIntermediateService() {
        Course newCourse = new Course("Verilog", 9000);
        entityManager.persist(newCourse);
        intermediateService.failToInsertExistingCategoryInAnotherTxWithShutUpCatch();
        return ResponseEntity.ok(newCourse);
    }

    @Transactional
    @GetMapping("/same-tx-with-shut-up-catch-in-intermediate-service")
    public ResponseEntity<Course> sameTxWithShutUpCatchInIntermediateService() {
        Course newCourse = new Course("Smalltalk", 40);
        entityManager.persist(newCourse);
        intermediateService.failToInsertExistingCategoryWithSameTxWithShutUpCatch();
        return ResponseEntity.ok(newCourse);
    }

}
