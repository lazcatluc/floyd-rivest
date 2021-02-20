package ro.contezi.floyd.rivest;

import static org.assertj.core.api.StrictAssertions.assertThat;

import java.util.Arrays;
import org.junit.Test;
import ro.contezi.floyd.rivest.APaginator;
import ro.contezi.floyd.rivest.Selector;
import ro.contezi.floyd.rivest.selector.ParallelElementPositionSelector;

public class ParallelElementPositionSelectorTest {

    @Test
    public void findsElementInBuggyList() {
        Selector<Integer> integerSelector = APaginator.of(Integer.class)
                .withData(Arrays.asList(63, 10, 20, 15, 44, 38, 52, 13, 30, 97, 28, 53, 43, 88, 71, 67, 24, 70, 14, 11, 33, 59, 64, 68, 21, 19, 17, 1, 89, 58, 81, 57, 16, 37, 2, 46, 36, 25, 75, 48, 8, 35, 4, 39, 32, 74, 85, 51, 86, 69, 78, 94, 95, 96, 40, 27, 87, 55, 50, 66, 41, 45, 80, 29, 26, 3, 99, 7, 23, 9, 73, 82, 5, 79, 47, 65, 90, 98, 22, 93, 77, 54, 76, 60, 34, 84, 31, 91, 18, 83, 6, 56, 12, 0, 92, 62, 61, 42, 72, 49)).withSelector(ParallelElementPositionSelector.class)
                .makeSelector();
        assertThat(integerSelector.find(10)).isEqualTo(10);
        assertThat(integerSelector.find(10)).isEqualTo(10);
    }
}