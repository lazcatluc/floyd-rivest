package ro.contezi.floyd.rivest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Comparator;

import org.junit.Test;

public class SelectorTest {
    @Test
    public void updatesRankAccordingToPartitionSize() throws Exception {
        Collection<Integer> data = PartitionTest.ORIGINAL;
        Comparator<Integer> comparator = (e1, e2) -> e1.compareTo(e2);
        
        KthElementSelector<Integer> selector = new KthElementSelector<>(data, new SquareRootPartition<>(data),
                comparator);
        
        assertThat(selector.partitionRank(4)).isEqualTo(0);
        assertThat(selector.partitionRank(10)).isEqualTo(2);
        assertThat(selector.partitionRank(11)).isEqualTo(2);
    }
}
