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

import ro.contezi.floyd.rivest.paginator.APaginator;
import ro.contezi.floyd.rivest.paginator.DoubleSelectorPaginator;
import ro.contezi.floyd.rivest.paginator.SortingPaginator;
import ro.contezi.floyd.rivest.partition.FixedSize;
import ro.contezi.floyd.rivest.partition.SquareRoot;
import ro.contezi.floyd.rivest.selector.KthElementSelector;

public class ElementSelectorITest {

    private List<Integer> data;
    private List<Integer> sample;
    private AtomicLong comparissons;
    private Comparator<Integer> comparator;
    private APaginator<Integer> aPaginator;
    private int pageSize;

    @Before
    public void setUp() {
        data = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
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
        aPaginator = APaginator.of(Integer.class, comparator).withData(data);
    }

    protected void assertFindsElementsInSample(Selector<Integer> selector) {
        for (Integer i : sample) {
            assertThat(selector.find(i)).isEqualTo(i);
        }
        System.out.println(comparissons.get() / data.size() / sample.size());
    }

    @Test
    @Ignore
    public void findsElementsNonRecursiveSquareRoot() throws Exception {
        assertFindsElementsInSample(aPaginator.withSelector(KthElementSelector.class).withPartitioner(SquareRoot.class)
                .makeSelector());
    }

    @Test
    public void findsPagesInPaginator() throws Exception {
        sample.stream()
                .mapToInt(i -> i / pageSize)
                .forEach(
                        i -> {
                            Collections.shuffle(data);

                            List<Integer> paged = aPaginator.ofClass(DoubleSelectorPaginator.class).make().getPage(i, pageSize);
                            assertThat(paged.get(0) % pageSize).isZero();
                            assertThat(paged.size()).isLessThanOrEqualTo(pageSize);
                        });

        System.out.println(comparissons.get() / data.size() / sample.size());
    }

    @Test
    public void findsPagesInSortingPaginator() throws Exception {
        sample.stream().mapToInt(i -> i / pageSize).forEach(i -> {
            Collections.shuffle(data);
            List<Integer> paged = aPaginator.ofClass(SortingPaginator.class).make().getPage(i, pageSize);
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
                            List<Integer> paged = aPaginator.make().getPage(i, pageSize);
                            assertThat(paged.get(0) % pageSize).isZero();
                            assertThat(paged.size()).isLessThanOrEqualTo(pageSize);
                        });

        System.out.println(comparissons.get() / data.size() / sample.size());
    }

    @Test
    public void findsElementsRecursiveLog2() throws Exception {
        assertFindsElementsInSample(aPaginator.makeSelector());
    }

    @Test
    public void findsElementsRecursiveFixedSize20() throws Exception {
        assertFindsElementsInSample(aPaginator.withPartitioner(FixedSize.class).withFixedSize(20).makeSelector());
    }

    @Test
    public void findsElementsRecursiveFixedSize200() throws Exception {
        assertFindsElementsInSample(fixedSizeSelector(200));
    }

    @Test
    public void findsElementsRecursiveFixedSize2000() throws Exception {
        assertFindsElementsInSample(fixedSizeSelector(2000));
    }

    @Test
    public void findsElementsRecursiveFixedSize600() throws Exception {
        assertFindsElementsInSample(fixedSizeSelector(600));
    }

    @Test
    public void findsElementsRecursiveFixedSize1000() throws Exception {
        assertFindsElementsInSample(fixedSizeSelector(1000));
    }

    @Test
    public void findsElementsRecursiveFixedSize1500() throws Exception {
        assertFindsElementsInSample(fixedSizeSelector(1500));
    }
    
    protected Selector<Integer> fixedSizeSelector(final int fixedSize) {
        return aPaginator.withPartitioner(FixedSize.class).withFixedSize(fixedSize).makeSelector();
    }
}
