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

    public CoursesController(EntityManager entityManager, CategoryService categoryService) {
        this.entityManager = entityManager;
        this.categoryService = categoryService;
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

}
