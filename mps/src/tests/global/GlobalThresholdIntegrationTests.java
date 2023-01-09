package tests.global;

import utils.BestTreeManager;
import global.FileSet;
import global.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class GlobalThresholdIntegrationTests extends AbstractGlobalTests {

    @Test
    void WHEN_trainFirstTree_THEN_saveTree() throws IOException {
        BestTreeManager.GLOBAL.reset();
        Main.train();

        Assertions.assertNotNull(BestTreeManager.GLOBAL.getTree());
    }

    @Test
    void WHEN_trainTree_THEN_saveOnlyIfItIsBetter() throws IOException {
        generateAndSaveBestTree();
        var performance1 = Main.testTreePerformance(BestTreeManager.GLOBAL.getTree(), FileSet.testSet);

        Main.train();
        var performance2 = Main.testTreePerformance(BestTreeManager.GLOBAL.getTree(), FileSet.testSet);
        Assertions.assertTrue(performance2 > performance1 || Math.abs(performance2 - performance1) <= 0.0002);

        Main.train();
        var performance3 = Main.testTreePerformance(BestTreeManager.GLOBAL.getTree(), FileSet.testSet);
        Assertions.assertTrue(performance3 > performance2 || Math.abs(performance2 - performance3) <= 0.0002);


        Main.train();
        var performance4 = Main.testTreePerformance(BestTreeManager.GLOBAL.getTree(), FileSet.testSet);
        Assertions.assertTrue(performance4 > performance3 || Math.abs(performance4 - performance3) <= 0.0002);
    }
}
