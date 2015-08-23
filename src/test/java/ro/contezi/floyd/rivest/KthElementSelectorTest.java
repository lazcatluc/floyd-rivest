package ro.contezi.floyd.rivest;

import static org.assertj.core.api.StrictAssertions.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import ro.contezi.floyd.rivest.partition.SquareRoot;
import ro.contezi.floyd.rivest.selector.KthElementSelector;
import ro.contezi.floyd.rivest.selector.PartitionSelector;

public class KthElementSelectorTest {
    private Collection<Integer> data;
    private APaginator<Integer> aPaginator;

    @Before
    public void setUp() {
        data = PartitionTest.ORIGINAL;
        aPaginator = APaginator.of(Integer.class).withData(data).withSelector(KthElementSelector.class)
                .withPartitioner(SquareRoot.class);
    }

    @Test
    public void updatesRankAccordingToPartitionSize() throws Exception {
        PartitionSelector<Integer> selector = (PartitionSelector<Integer>) aPaginator.makeSelector();

        assertThat(selector.partitionRank(4)).isEqualTo(1);
        assertThat(selector.partitionRank(10)).isEqualTo(2);
        assertThat(selector.partitionRank(11)).isEqualTo(3);
    }

    @Test
    public void finds2ndElementInSpecificPartition() throws Exception {
        Selector<Integer> selector = aPaginator.withPartition(new Partition<Integer>() {
            @Override
            public Collection<Integer> partition() {
                return Arrays.asList(2, 6, 10, 14, 25);
            }
        }).makeSelector();
        assertThat(selector.find(2)).isEqualTo(3);
    }

    @Test
    public void findsElements() throws Exception {
        Selector<Integer> selector = aPaginator.makeSelector();

        for (int i = 1; i < data.size(); i++) {
            assertThat(selector.find(i)).isEqualTo(i + 1);
        }
    }
}
