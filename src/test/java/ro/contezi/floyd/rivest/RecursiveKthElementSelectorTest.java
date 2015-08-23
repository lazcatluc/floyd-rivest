package ro.contezi.floyd.rivest;

import static org.assertj.core.api.StrictAssertions.assertThat;

import java.util.Collection;
import java.util.Comparator;

import org.junit.Test;

import ro.contezi.floyd.rivest.partition.FixedSize;
import ro.contezi.floyd.rivest.partition.Log2;
import ro.contezi.floyd.rivest.selector.PartitionSelector;
import ro.contezi.floyd.rivest.selector.RecursiveKthElementSelector;

public class RecursiveKthElementSelectorTest {
    @Test
    public void updatesRankAccordingToPartitionSize() throws Exception {
        Collection<Integer> data = PartitionTest.ORIGINAL;
        Comparator<Integer> comparator = (e1, e2) -> e1.compareTo(e2);

        PartitionSelector<Integer> selector = new RecursiveKthElementSelector<Integer>(data, Log2<Integer>::new,
                comparator);

        assertThat(selector.partitionRank(4)).isEqualTo(0);
        assertThat(selector.partitionRank(10)).isEqualTo(1);
        assertThat(selector.partitionRank(11)).isEqualTo(1);
    }

    @Test
    public void findsElements() throws Exception {
        Collection<Integer> data = PartitionTest.ORIGINAL;
        Comparator<Integer> comparator = (e1, e2) -> e1.compareTo(e2);

        PartitionSelector<Integer> selector = new RecursiveKthElementSelector<Integer>(data, Log2<Integer>::new,
                comparator);

        assertThat(selector.find(0)).isEqualTo(1);
        for (int i = 1; i < data.size(); i++) {
            assertThat(selector.find(i)).isEqualTo(i + 1);
        }
    }
    
    @Test
    public void findsElementsWithFixedSize() throws Exception {
        Collection<Integer> data = PartitionTest.ORIGINAL;
        Comparator<Integer> comparator = (e1, e2) -> e1.compareTo(e2);

        PartitionSelector<Integer> selector = new RecursiveKthElementSelector<Integer>(data, FixedSize.supplier(20),
                comparator);

        assertThat(selector.find(0)).isEqualTo(1);
        for (int i = 1; i < data.size(); i++) {
            assertThat(selector.find(i)).isEqualTo(i + 1);
        }
    }
}
