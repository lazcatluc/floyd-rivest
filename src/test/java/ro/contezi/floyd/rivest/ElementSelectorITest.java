package ro.contezi.floyd.rivest;

import static org.assertj.core.api.StrictAssertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ro.contezi.floyd.rivest.paginator.DoubleSelectorPaginator;
import ro.contezi.floyd.rivest.paginator.RecursiveKthElementPaginator;
import ro.contezi.floyd.rivest.paginator.SortingPaginator;
import ro.contezi.floyd.rivest.partition.FixedSize;
import ro.contezi.floyd.rivest.partition.Log2;
import ro.contezi.floyd.rivest.partition.SquareRoot;
import ro.contezi.floyd.rivest.selector.KthElementSelector;
import ro.contezi.floyd.rivest.selector.PartitionSelector;
import ro.contezi.floyd.rivest.selector.RecursiveKthElementSelector;

public class ElementSelectorITest {

    private List<Integer> data;
    private List<Integer> sample;
    private AtomicLong comparissons;
    private Comparator<Integer> comparator;
    private int pageSize;

    @Before
    public void setUp() {
        data = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            data.add(i);
        }
        Collections.shuffle(data);
        sample = data.subList(0, 100);
        comparissons = new AtomicLong(0);
        comparator = (e1, e2) -> {
            comparissons.incrementAndGet();
            return e1.compareTo(e2);
        };
        pageSize = 10;
    }

    protected void assertFindsElementsInSample(PartitionSelector<Integer> selector) {
        for (Integer i : sample) {
            assertThat(selector.find(i)).isEqualTo(i);
        }
        System.out.println(comparissons.get() / data.size() / sample.size());
    }

    @Test
    @Ignore
    public void findsElementsNonRecursiveSquareRoot() throws Exception {
        assertFindsElementsInSample(new KthElementSelector<>(data, new SquareRoot<>(data), comparator));
    }

    @Test
    public void findsPagesInPaginator() throws Exception {
        sample.stream()
                .mapToInt(i -> i / pageSize)
                .forEach(
                        i -> {
                            Collections.shuffle(data);
                            List<Integer> paged = new DoubleSelectorPaginator<>(data,
                                    new RecursiveKthElementSelector<Integer>(data, Log2<Integer>::new, comparator),
                                    comparator).getPage(i, pageSize);
                            assertThat(paged.get(0) % pageSize).isZero();
                            assertThat(paged.size()).isLessThanOrEqualTo(pageSize);
                        });

        System.out.println(comparissons.get() / data.size() / sample.size());
    }

    @Test
    public void findsPagesInSortingPaginator() throws Exception {
        sample.stream().mapToInt(i -> i / pageSize).forEach(i -> {
            Collections.shuffle(data);
            List<Integer> paged = new SortingPaginator<>(data, comparator).getPage(i, pageSize);
            assertThat(paged.get(0) % pageSize).isZero();
            assertThat(paged.size()).isLessThanOrEqualTo(pageSize);
        });

        System.out.println(comparissons.get() / data.size() / sample.size());
    }

    @Test
    public void findsPagesInRecursivePaginator() throws Exception {
        sample.stream()
                .mapToInt(i -> i / pageSize)
                .forEach(
                        i -> {
                            Collections.shuffle(data);
                            RecursiveKthElementPaginator<Integer> paginator = new RecursiveKthElementPaginator<Integer>(
                                    data, Log2<Integer>::new, comparator);
                            List<Integer> paged = paginator.getPage(i, pageSize);
                            assertThat(paged.get(0) % pageSize).isZero();
                            assertThat(paged.size()).isLessThanOrEqualTo(pageSize);
                        });

        System.out.println(comparissons.get() / data.size() / sample.size());
    }

    @Test
    public void findsElementsRecursiveLog2() throws Exception {
        assertFindsElementsInSample(new RecursiveKthElementSelector<Integer>(data, Log2<Integer>::new, comparator));
    }

    @Test
    public void findsElementsRecursiveFixedSize20() throws Exception {
        assertFindsElementsInSample(new RecursiveKthElementSelector<Integer>(data, FixedSize.supplier(20), comparator));
    }

    @Test
    public void findsElementsRecursiveFixedSize200() throws Exception {
        assertFindsElementsInSample(new RecursiveKthElementSelector<Integer>(data, FixedSize.supplier(200), comparator));
    }

    @Test
    public void findsElementsRecursiveFixedSize2000() throws Exception {
        assertFindsElementsInSample(new RecursiveKthElementSelector<Integer>(data, FixedSize.supplier(2000), comparator));
    }

    @Test
    public void findsElementsRecursiveFixedSize600() throws Exception {
        assertFindsElementsInSample(new RecursiveKthElementSelector<Integer>(data, FixedSize.supplier(600), comparator));
    }

    @Test
    public void findsElementsRecursiveFixedSize1000() throws Exception {
        assertFindsElementsInSample(new RecursiveKthElementSelector<Integer>(data, FixedSize.supplier(1000), comparator));
    }

    @Test
    public void findsElementsRecursiveFixedSize1500() throws Exception {
        assertFindsElementsInSample(new RecursiveKthElementSelector<Integer>(data, FixedSize.supplier(1500), comparator));
    }
}
