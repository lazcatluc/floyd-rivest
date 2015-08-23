package ro.contezi.floyd.rivest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.StrictAssertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;

import ro.contezi.floyd.rivest.paginator.DoubleSelectorPaginator;
import ro.contezi.floyd.rivest.paginator.RecursiveKthElementPaginator;
import ro.contezi.floyd.rivest.paginator.SortingPaginator;
import ro.contezi.floyd.rivest.partition.Log2;

public class PaginatorTest {

    private APaginator<Integer> aPaginator;
    private Collection<Integer> data;

    @Before
    public void setUp() {
        data = PartitionTest.ORIGINAL;
        aPaginator = APaginator.of(Integer.class).withData(data);
    }

    @Test
    public void page1With5ResultsIs6Through10() throws Exception {
        assertThat(aPaginator.ofClass(DoubleSelectorPaginator.class).make().getPage((long) 1, (long) 5)).isEqualTo(
                Arrays.asList(6, 7, 8, 9, 10));
    }

    @Test
    public void page1With5ResultsIs6Through10BySorting() throws Exception {
        assertThat(aPaginator.ofClass(SortingPaginator.class).make().getPage((long) 1, (long) 5)).isEqualTo(
                Arrays.asList(6, 7, 8, 9, 10));
    }

    @Test
    public void page1With5ResultsIs6Through10Recursively() throws Exception {
        assertThat(aPaginator.make().getPage((long) 1, (long) 5)).isEqualTo(
                Arrays.asList(6, 7, 8, 9, 10));
    }

    @Test
    public void page0With3ResultsIs1Through3ecursively() throws Exception {
        assertThat(aPaginator.make().getPage((long) 0, (long) 3)).isEqualTo(Arrays.asList(1, 2, 3));
    }

    @Test
    public void page3With7ResultsIs22Through24Recursively() throws Exception {
        assertThat(aPaginator.make().getPage((long) 3, (long) 7)).isEqualTo(Arrays.asList(22, 23, 24, 25));
    }

    @Test
    public void findsThirdPageWithSpecialPartition() throws Exception {
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add(i);
        }
        Collections.shuffle(data);
        Comparator<Integer> comparator = (e1, e2) -> e1.compareTo(e2);

        int pageSize = 10;
        List<Integer> partition = Arrays.asList(11, 18, 61, 62, 76, 80);
        Function<Collection<Integer>, Partition<Integer>> partitioner = (myData) -> {
            if (myData == data)
                return new Partition<Integer>() {
                    @Override
                    public Collection<Integer> partition() {
                        return partition;
                    }
                };
            return new Log2<>(myData);
        };

        RecursiveKthElementPaginator<Integer> paginator = new RecursiveKthElementPaginator<Integer>(data, partitioner,
                comparator);
        assertThat(paginator.getPage(3, pageSize).isEmpty()).isFalse();
    }
}
