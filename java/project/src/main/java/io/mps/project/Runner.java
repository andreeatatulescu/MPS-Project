package io.mps.project;

import io.mps.project.tree.services.TreeTrainer;

import java.util.Date;

public class Runner {

    private static final TreeTrainer treeTrainer = TreeTrainer.getInstance();

    public static void main(String[] args) {
        long start = new Date().getTime();
        treeTrainer.train(10);
        long end = new Date().getTime();

        System.out.println("TIME: " + ((end - start) / 1000.0));
    }
}
