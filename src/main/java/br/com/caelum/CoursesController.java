package br.com.caelum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

@RestController
public class CoursesController {

    private static final Logger logger = LoggerFactory.getLogger(CoursesController.class);

    private final EntityManager entityManager;
    private final CategoryService categoryService;
    private final IntermediateService intermediateService;

    public CoursesController(EntityManager entityManager, CategoryService categoryService, IntermediateService intermediateService) {
        this.entityManager = entityManager;
        this.categoryService = categoryService;
        this.intermediateService = intermediateService;
    }

    @Transactional
    @GetMapping("/same-tx-without-shut-up-catch")
    public ResponseEntity<Course> sameTxWithoutShutUpCatch() {
        Course newCourse = new Course("Java", 40);
        Assert.isNull(newCourse.getId(), "Can't add a course that already has an id.");
        entityManager.persist(newCourse);
        categoryService.failToInsertExistingCategoryWithSameTx();
        return ResponseEntity.ok(newCourse);
    }

    @Transactional
    @GetMapping("/same-tx-with-shut-up-catch")
    public ResponseEntity<Course> sameTxWithShutUpCatch() {
        Course newCourse = new Course("Python", 40);
        entityManager.persist(newCourse);
        try {
            categoryService.failToInsertExistingCategoryWithSameTx();
        } catch (PersistenceException ex) {
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
            categoryService.failToInsertExistingCategoryInAnotherTx();
        } catch (PersistenceException ex) {
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
        categoryService.failToInsertExistingCategoryInAnotherTxWithShutUpCatch();
        return ResponseEntity.ok(newCourse);
    }

    @Transactional
    @GetMapping("/another-tx-with-shut-up-catch-inside-but-in-another-method")
    public ResponseEntity<Course> anotherTxWithShutUpCatchInsideButInAnotherMethod() {
        Course newCourse = new Course("Lisp", 2600);
        entityManager.persist(newCourse);
        categoryService.failToInsertExistingCategoryInAnotherTxWithShutUpCatchInAnotherMethod();
        return ResponseEntity.ok(newCourse);
    }

    @Transactional
    @GetMapping("/another-tx-with-shut-up-catch-and-no-rollback-for-persistence-exception-inside")
    public ResponseEntity<Course> anotherTxWithShutUpCatchInsideWithNoRollbackForPersistenceException() {
        Course newCourse = new Course("JavaScript", 2);
        entityManager.persist(newCourse);
        categoryService.failToInsertExistingCategoryInAnotherTxWithNoRollbackForPersistenceExceptionWithShutUpCatch();
        return ResponseEntity.ok(newCourse);
    }

    @Transactional(noRollbackFor = PersistenceException.class)
    @GetMapping("/another-tx-with-shut-up-catch-inside-but-with-no-rollback-for-persistence-exception-outside")
    public ResponseEntity<Course> a() {
        Course newCourse = new Course("Rust", 3200);
        entityManager.persist(newCourse);
        categoryService.failToInsertExistingCategoryInAnotherTxWithShutUpCatch();
        return ResponseEntity.ok(newCourse);
    }

    @Transactional(noRollbackFor = PersistenceException.class)
    @GetMapping("/another-tx-with-shut-up-catch-and-no-rollback-for-persistence-exception-outside")
    public ResponseEntity<Course> b() {
        Course newCourse = new Course("Fortran", 80);
        entityManager.persist(newCourse);
        try {
            categoryService.failToInsertExistingCategoryInAnotherTx();
        } catch (PersistenceException ex) {
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
