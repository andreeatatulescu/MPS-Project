package tests.global;

import utils.BestTreeManager;
import global.FileSet;
import global.Main;

class AbstractGlobalTests {

    protected AbstractGlobalTests() {
        BestTreeManager.GLOBAL.setFileName("testBestTree.txt");
    }

    protected void generateAndSaveBestTree() {
        BestTreeManager.GLOBAL.reset();
        var tree = Main.generateRandomTree();
        var performance = Main.testTreePerformance(tree, FileSet.testSet);
        BestTreeManager.GLOBAL.changeIfBetter(tree, performance);
    }
}
