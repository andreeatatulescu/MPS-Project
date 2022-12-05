package io.mps.project.tree.services;

import com.google.common.collect.Iterables;
import io.mps.project.image.Image;
import io.mps.project.image.ImageIOService;
import io.mps.project.tree.models.*;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Collection;
import java.util.List;

public class TreeEvaluator {
    private static final int CHUNK_SIZE = 1000;

    private final ImageIOService imageService = ImageIOService.getInstance();


    public double train(Node tree) {
        return evaluate(tree, imageService.getTrainSet());
    }

    public double validate(Node tree) {
        return evaluate(tree, imageService.getValidationSet());
    }

    public double test(Node tree) {
        return evaluate(tree, imageService.getTestSet());
    }

    public double evaluate(Node tree, Collection<Image> data) {
        Double performance = Flux.<List<Image>>create(sink -> {
                    Iterables.partition(data, CHUNK_SIZE).forEach(sink::next);
                    sink.complete();
                })
                .parallel()
                .runOn(Schedulers.parallel())
                .map(images -> images.stream()
                        .map(image -> image.measure(tree.calculate(image)))
                        .reduce(0.0, Double::sum)
                )
                .sequential()
                .reduce(0.0, Double::sum)
                .block();

        if (performance == null) {
            throw new NullPointerException();
        }

        return performance / data.size();
    }

    public int complexity(Node node) {
        if (node instanceof AtomNode) {
            return 1;
        }
        if (node instanceof IfNode ifNode) {
            return 1 + Math.max(complexity(ifNode.getIfFalse()), complexity(ifNode.getIfTrue()));
        }
        if (node instanceof ForNode forNode) {
            return 1 + forNode.getChildren().stream().mapToInt(this::complexity).sum();
        }
        throw new IllegalArgumentException();
    }

    private TreeEvaluator() {}

    private static final TreeEvaluator INSTANCE = new TreeEvaluator();

    public static TreeEvaluator getInstance() {
        return INSTANCE;
    }
}
