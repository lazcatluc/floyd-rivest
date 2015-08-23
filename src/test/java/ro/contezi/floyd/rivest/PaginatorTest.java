package ro.contezi.floyd.rivest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class PaginatorTest {
    @Test
    public void page1With5ResultsIs6Through10() throws Exception {
        Comparator<? super Integer> comparator = (i1, i2) -> i1.compareTo(i2);
        Collection<Integer> data = PartitionTest.ORIGINAL;
        PartitionSelector<Integer> selector = new RecursiveKthElementSelector<Integer>(data,
                Log2Partition<Integer>::new, comparator);
        Paginator<Integer> paginator = new Paginator<>(data, selector, comparator);

        final long page = 1;
        final long pageSize = 5;
        List<Integer> paged = paginator.getPage(page, pageSize);

        assertThat(paged).isEqualTo(Arrays.asList(6, 7, 8, 9, 10));
    }
}
