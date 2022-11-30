package io.mps.project.tree.services;

import io.mps.project.image.ThresholdType;
import io.mps.project.tree.models.*;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

public class TreeTrainer {

    private static final Random random = new Random();
    private final BestTreeManager bestTreeManager = BestTreeManager.getInstance();


    public void train(int cycles) {
        Flux.<Integer>create(sink -> {
                    IntStream.range(0, cycles)
                            .map(index -> random.nextInt(5, 10))
                            .forEach(sink::next);

                    sink.complete();
                })
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .filter(Objects::nonNull)
                .doOnNext(this::trainTree)
                .sequential()
                .collectList()
                .block();


    }

    private void trainTree(int depth) {
        long start = new Date().getTime();
        Node tree = randomNode(depth);
        long end = new Date().getTime();

        System.out.println("Generation Time " + (end - start) / 1000.0);

        bestTreeManager.changeIfBetter(tree);

        System.out.println("Validation Time: " + (new Date().getTime() - end) / 1000.0);
    }

    private Node randomNode(int depth) {
        if (depth == 0) {
            return randomAtom();
        }

        int type = random.nextInt(2);
        if (type == 0) {
            return randomIf(depth);
        }
        return randomFor(depth);
    }

    private IfNode randomIf(int depth) {
        Condition condition = randomCondition();
        Node ifTrue = randomNode(depth - 1);
        Node ifFalse = randomNode(depth - 1);

        return new IfNode(condition, ifTrue, ifFalse);
    }

    private Condition randomCondition() {
        Node node = randomAtom();
        Condition.Type[] types = Condition.Type.values();
        Condition.Type type = types[random.nextInt(types.length)];
        double reference = random.nextDouble();

        return new Condition(node, type, reference);
    }

    private ForNode randomFor(int depth) {
        int childrenSize = random.nextInt(3, 7);
        List<Node> children = IntStream.range(0, childrenSize)
                .mapToObj(unused -> randomNode(depth - 1))
                .toList();

        OperationType operationType = OperationType.values()[random.nextInt(OperationType.values().length)];

        return new ForNode(children, operationType);
    }

    private AtomNode randomAtom() {
        var thresholdType = ThresholdType.valueOf(random.nextInt(ThresholdType.count()));

        return new AtomNode(thresholdType);
    }

    private TreeTrainer() {}

    private static final TreeTrainer INSTANCE = new TreeTrainer();

    public static TreeTrainer getInstance() {
        return INSTANCE;
    }
}
