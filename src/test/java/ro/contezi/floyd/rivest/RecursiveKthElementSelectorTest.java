package ro.contezi.floyd.rivest;

import static org.assertj.core.api.StrictAssertions.assertThat;

import org.junit.Test;

import ro.contezi.floyd.rivest.selector.PartitionSelector;

public class RecursiveKthElementSelectorTest {
    @Test
    public void updatesRankAccordingToPartitionSize() throws Exception {
        PartitionSelector<Integer> selector = (PartitionSelector<Integer>)mySelector();

        assertThat(selector.partitionRank(4)).isEqualTo(0);
        assertThat(selector.partitionRank(10)).isEqualTo(1);
        assertThat(selector.partitionRank(11)).isEqualTo(1);
    }

    @Test
    public void findsElements() throws Exception {
        Selector<Integer> selector = mySelector();

        assertThat(selector.find(0)).isEqualTo(1);
        for (int i = 1; i < PartitionTest.ORIGINAL.size(); i++) {
            assertThat(selector.find(i)).isEqualTo(i + 1);
        }
    }
    
    @Test
    public void findsElementsWithFixedSize() throws Exception {
        Selector<Integer> selector = mySelector();

        assertThat(selector.find(0)).isEqualTo(1);
        for (int i = 1; i < PartitionTest.ORIGINAL.size(); i++) {
            assertThat(selector.find(i)).isEqualTo(i + 1);
        }
    }
    
    protected Selector<Integer> mySelector() {
        return APaginator.of(Integer.class).withData(PartitionTest.ORIGINAL).makeSelector();
    }
}
