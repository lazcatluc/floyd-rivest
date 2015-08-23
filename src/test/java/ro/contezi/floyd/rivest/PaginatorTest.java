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

import org.junit.Test;

import ro.contezi.floyd.rivest.paginator.DoubleSelectorPaginator;
import ro.contezi.floyd.rivest.paginator.RecursiveKthElementPaginator;
import ro.contezi.floyd.rivest.paginator.SortingPaginator;
import ro.contezi.floyd.rivest.partition.Log2;
import ro.contezi.floyd.rivest.selector.PartitionSelector;
import ro.contezi.floyd.rivest.selector.RecursiveKthElementSelector;

public class PaginatorTest {
    @Test
    public void page1With5ResultsIs6Through10() throws Exception {
        Comparator<? super Integer> comparator = (i1, i2) -> i1.compareTo(i2);
        Collection<Integer> data = PartitionTest.ORIGINAL;
        PartitionSelector<Integer> selector = new RecursiveKthElementSelector<Integer>(data, Log2<Integer>::new,
                comparator);
        DoubleSelectorPaginator<Integer> paginator = new DoubleSelectorPaginator<>(data, selector, comparator);

        final long page = 1;
        final long pageSize = 5;
        List<Integer> paged = paginator.getPage(page, pageSize);

        assertThat(paged).isEqualTo(Arrays.asList(6, 7, 8, 9, 10));
    }

    @Test
    public void page1With5ResultsIs6Through10BySorting() throws Exception {
        Comparator<? super Integer> comparator = (i1, i2) -> i1.compareTo(i2);
        List<Integer> data = PartitionTest.ORIGINAL;

        Paginator<Integer> paginator = new SortingPaginator<>(data, comparator);

        final long page = 1;
        final long pageSize = 5;
        List<Integer> paged = paginator.getPage(page, pageSize);

        assertThat(paged).isEqualTo(Arrays.asList(6, 7, 8, 9, 10));
    }

    @Test
    public void page1With5ResultsIs6Through10Recursively() throws Exception {
        Comparator<? super Integer> comparator = (i1, i2) -> i1.compareTo(i2);
        List<Integer> data = PartitionTest.ORIGINAL;

        Paginator<Integer> paginator = new RecursiveKthElementPaginator<Integer>(data, Log2<Integer>::new, comparator);

        final long page = 1;
        final long pageSize = 5;
        List<Integer> paged = paginator.getPage(page, pageSize);

        assertThat(paged).isEqualTo(Arrays.asList(6, 7, 8, 9, 10));
    }

    @Test
    public void page0With3ResultsIs1Through3ecursively() throws Exception {
        Comparator<? super Integer> comparator = (i1, i2) -> i1.compareTo(i2);
        List<Integer> data = PartitionTest.ORIGINAL;

        Paginator<Integer> paginator = new RecursiveKthElementPaginator<Integer>(data, Log2<Integer>::new, comparator);

        final long page = 0;
        final long pageSize = 3;
        List<Integer> paged = paginator.getPage(page, pageSize);

        assertThat(paged).isEqualTo(Arrays.asList(1, 2, 3));
    }
    
    @Test
    public void page3With7ResultsIs22Through24Recursively() throws Exception {
        Comparator<? super Integer> comparator = (i1, i2) -> i1.compareTo(i2);
        List<Integer> data = PartitionTest.ORIGINAL;

        Paginator<Integer> paginator = new RecursiveKthElementPaginator<Integer>(data, Log2<Integer>::new, comparator);

        final long page = 3;
        final long pageSize = 7;
        List<Integer> paged = paginator.getPage(page, pageSize);

        assertThat(paged).isEqualTo(Arrays.asList(22, 23, 24, 25));
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
                return new Partition<Integer>(){
                    @Override
                    public Collection<Integer> partition() {
                        return partition;
                    }
                }; 
                return new Log2<>(myData);
        };
        
        RecursiveKthElementPaginator<Integer> paginator = new RecursiveKthElementPaginator<Integer>(
                data, partitioner, comparator);
        assertThat(paginator.getPage(3, pageSize).isEmpty()).isFalse();
    }    
}
