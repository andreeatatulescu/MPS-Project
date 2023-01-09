package tests.local;

import local.FileSet;
import local.MainL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.BestTreeManager;

import java.io.IOException;

public class LocalThresholdIntegrationTests extends AbstractLocalTests {

    @Test
    void WHEN_trainFirstTree_THEN_saveTree() throws IOException {
        BestTreeManager.LOCAL.reset();
        MainL.train();

        Assertions.assertNotNull(BestTreeManager.LOCAL.getTree());
    }

    @Test
    void WHEN_trainTree_THEN_saveOnlyIfItIsBetter() throws IOException {
        generateAndSaveBestTree();
        var performance1 = MainL.testTreePerformance(BestTreeManager.LOCAL.getTree(), FileSet.testSet);

        MainL.train();
        var performance2 = MainL.testTreePerformance(BestTreeManager.LOCAL.getTree(), FileSet.testSet);
        Assertions.assertTrue(performance2 > performance1 || Math.abs(performance2 - performance1) <= 2);

        MainL.train();
        var performance3 = MainL.testTreePerformance(BestTreeManager.LOCAL.getTree(), FileSet.testSet);
        Assertions.assertTrue(performance3 > performance2 || Math.abs(performance2 - performance3) <= 2);


        MainL.train();
        var performance4 = MainL.testTreePerformance(BestTreeManager.LOCAL.getTree(), FileSet.testSet);
        Assertions.assertTrue(performance4 > performance3 || Math.abs(performance4 - performance3) <= 2);
    }

    protected void generateAndSaveBestTree() {
        BestTreeManager.LOCAL.reset();
        var tree = MainL.generateRandomTree();
        var performance = MainL.testTreePerformance(tree, FileSet.testSet);
        BestTreeManager.LOCAL.changeIfBetter(tree, performance);
    }
}
