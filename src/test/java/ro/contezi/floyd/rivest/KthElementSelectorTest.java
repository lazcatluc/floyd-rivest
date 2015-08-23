package ro.contezi.floyd.rivest;

import static org.assertj.core.api.StrictAssertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import org.junit.Test;

public class KthElementSelectorTest {
    @Test
    public void updatesRankAccordingToPartitionSize() throws Exception {
        Collection<Integer> data = PartitionTest.ORIGINAL;
        Comparator<Integer> comparator = (e1, e2) -> e1.compareTo(e2);

        PartitionSelector<Integer> selector = new KthElementSelector<>(data, new SquareRootPartition<>(data),
                comparator);

        assertThat(selector.partitionRank(4)).isEqualTo(1);
        assertThat(selector.partitionRank(10)).isEqualTo(2);
        assertThat(selector.partitionRank(11)).isEqualTo(3);
    }
    
    @Test
    public void finds2ndElementInSpecificPartition() throws Exception {
        Collection<Integer> data = PartitionTest.ORIGINAL;
        Comparator<Integer> comparator = (e1, e2) -> e1.compareTo(e2);
        Partition<Integer> partition = new Partition<Integer>(){
            @Override
            public Collection<Integer> partition() {
                return Arrays.asList(2, 6, 10, 14, 25);
            }
        };
        
        PartitionSelector<Integer> selector = new KthElementSelector<>(data, partition, comparator);
        assertThat(selector.find(2)).isEqualTo(3);
    }

    @Test
    public void findsElements() throws Exception {
        Collection<Integer> data = PartitionTest.ORIGINAL;
        Comparator<Integer> comparator = (e1, e2) -> e1.compareTo(e2);

        PartitionSelector<Integer> selector = new KthElementSelector<>(data, new SquareRootPartition<>(data),
                comparator);

        for (int i = 1; i < data.size(); i++) {
            assertThat(selector.find(i)).isEqualTo(i + 1);
        }
    }
}
