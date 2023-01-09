package tests.local;

import global.FileSet;
import local.MainL;
import utils.BestTreeManager;

public class AbstractLocalTests {

    protected AbstractLocalTests() {
        BestTreeManager.LOCAL.setFileName("testBestLocalTree.txt");
    }

    protected void generateAndSaveBestTree() {
        BestTreeManager.LOCAL.reset();
        var tree = MainL.generateRandomTree();
        var performance = MainL.testTreePerformance(tree, FileSet.testSet);
        BestTreeManager.LOCAL.changeIfBetter(tree, performance);
    }
}
