package stream.api;

import common.test.tool.annotation.Easy;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

public class Exercise6Test {

    @Easy
    @Test
    public void streamFromValues() {
        Stream<String> letters = Stream.of("a", "b", "c");
        List<String> list = letters.collect(Collectors.toList());
        assertThat(list, contains("a", "b", "c"));
    }

    @Easy
    @Test
    public void numberStream() {
        Stream<Integer> multiplesOfThree = Stream.iterate(0, i -> i + 3).limit(10);
        List<Integer> list = multiplesOfThree.collect(Collectors.toList());
        assertThat(list, contains(0, 3, 6, 9, 12, 15, 18, 21, 24, 27));
    }
}