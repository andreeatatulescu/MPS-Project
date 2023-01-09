package tests.local;

import local.ThreadUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ThreadUtilsUnitTests extends AbstractLocalTests {

    @Test
    void GIVEN_allPixels_WHEN_obtainScoresForThread_THEN_scoreIsCorrect() throws Exception {

        List<List<Float>> pixels = List.of(
                List.of(0f, 0f, 0f), List.of(1f, 1.0f, 0f), List.of(1f, 1.0f, 0f), List.of(1f, 1.0f, 0f),
                List.of(1f, 0f, 0f), List.of(1f, 1.0f, 0f), List.of(1f, 1.0f, 0f), List.of(1f, 1.0f, 0f),
                List.of(1f, 0f, 0f), List.of(1f, 1.0f, 0f), List.of(1f, 1.0f, 0f), List.of(1f, 1.0f, 0f),
                List.of(1f, 0f, 0f), List.of(1f, 1.0f, 0f), List.of(1f, 1.0f, 0f), List.of(1f, 1.0f, 0f)
        );

        var scores = new int[4];
        ThreadUtils.obtainScores(pixels, 0, List.of(0f), scores);

        Assertions.assertEquals(0, scores[0]);
        Assertions.assertEquals(0, scores[1]);
        Assertions.assertEquals(1, scores[2]);
        Assertions.assertEquals(0, scores[3]);
    }
}
