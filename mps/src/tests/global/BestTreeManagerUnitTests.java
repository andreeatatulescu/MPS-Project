package tests.global;


import utils.BestTreeManager;
import global.FileSet;
import global.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BestTreeManagerUnitTests extends AbstractGlobalTests {

    @Test
    void WHEN_bestTreeIsNotGenerated_THEN_return_null() {
        BestTreeManager.GLOBAL.reset();
        Assertions.assertNull(BestTreeManager.GLOBAL.getTree());
    }

    @Test
    void WHEN_bestTreeIsSaved_THEN_return_non_null() {
        generateAndSaveBestTree();
        Assertions.assertNotNull(BestTreeManager.GLOBAL.getTree());
    }

    @Test
    void WHEN_getBestTreeMultipleTimes_THEN_performanceIsSame() {
        generateAndSaveBestTree();

        var tree1 = BestTreeManager.GLOBAL.getTree();
        var performance1 = Main.testTreePerformance(tree1, FileSet.testSet);

        var tree2 = BestTreeManager.GLOBAL.getTree();
        var performance2 = Main.testTreePerformance(tree2, FileSet.testSet);

        Assertions.assertEquals(performance1, performance2, 0.0002);


        tree1 = BestTreeManager.GLOBAL.getTree();
        performance1 = Main.testTreePerformance(tree1, FileSet.testSet);

        tree2 = BestTreeManager.GLOBAL.getTree();
        performance2 = Main.testTreePerformance(tree2, FileSet.testSet);

        Assertions.assertEquals(performance1, performance2, 0.0002);
    }

    @Test
    void WHEN_bestTreeIsNotSaved_THEN_initWithNull() {
        BestTreeManager.GLOBAL.reset();
        BestTreeManager.GLOBAL.init();

        Assertions.assertNull(BestTreeManager.GLOBAL.getTree());
    }

    @Test
    void WHEN_bestTreeIsSaved_THEN_initWithNonNull() {
        generateAndSaveBestTree();
        BestTreeManager.GLOBAL.init();

        Assertions.assertNotNull(BestTreeManager.GLOBAL.getTree());
    }
}
