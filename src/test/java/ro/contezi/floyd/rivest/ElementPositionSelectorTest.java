package ro.contezi.floyd.rivest;

import static org.assertj.core.api.StrictAssertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import ro.contezi.floyd.rivest.partition.SquareRoot;
import ro.contezi.floyd.rivest.selector.ElementPositionSelector;
import ro.contezi.floyd.rivest.selector.KthElementSelector;
import ro.contezi.floyd.rivest.selector.PartitionSelector;

public class ElementPositionSelectorTest {
    private Collection<Integer> data;
    private APaginator<Integer> aPaginator;

    @Before
    public void setUp() {
        data = PartitionTest.ORIGINAL;
        aPaginator = APaginator.of(Integer.class).withData(data).withSelector(ElementPositionSelector.class);
    }

    @Test
    public void findsElements() {
        Selector<Integer> selector = aPaginator.makeSelector();

        for (int i = 1; i < data.size(); i++) {
            assertThat(selector.find(i)).isEqualTo(i + 1);
        }
    }

    @Test
    public void findsElementInBuggyList() {
        assertThat(APaginator.of(Integer.class)
                .withData(Arrays.asList(26, 78, 62, 2, 29, 21, 35, 6, 34, 99, 0, 23, 75, 44, 51, 85, 68, 5, 20, 73, 59, 40, 46, 13, 89, 53, 60, 72, 12, 16, 31, 36, 86, 32, 7, 25, 30, 39, 76, 71, 67, 87, 94, 41, 3, 57, 95, 48, 98, 79, 11, 83, 4, 27, 55, 80, 22, 24, 15, 17, 1, 82, 43, 47, 88, 54, 18, 28, 63, 81, 8, 92, 90, 84, 9, 66, 70, 19, 37, 58, 97, 45, 65, 14, 33, 61, 38, 91, 42, 74, 96, 10, 50, 77, 69, 49, 52, 93, 64, 56)).withSelector(ElementPositionSelector.class)
                .makeSelector().find(62)).isEqualTo(62);
    }
}
