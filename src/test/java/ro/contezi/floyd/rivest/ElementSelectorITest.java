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
import ro.contezi.floyd.rivest.paginator.SortingPaginator;
import ro.contezi.floyd.rivest.partition.FixedSize;
import ro.contezi.floyd.rivest.partition.SquareRoot;
import ro.contezi.floyd.rivest.selector.ElementPositionSelector;
import ro.contezi.floyd.rivest.selector.KthElementSelector;
import ro.contezi.floyd.rivest.selector.ParallelElementPositionSelector;

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
        aPaginator = APaginator.of(comparator).withData(data);
    }

    protected void assertFindsElementsInSample(String title, Selector<Integer> selector) throws Throwable {
        long start = System.currentTimeMillis();
        for (Integer i : sample) {
            try {
                assertThat(selector.find(i)).isEqualTo(i);
            } catch (Throwable re) {
                System.out.println("Failed for " + i + " in " + data);
                throw re;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(title + ": " + comparissons.get() / data.size() / sample.size()
                + " comparisson rate; " + ((end - start) / 1000) + " seconds");
    }

    @Test
    @Ignore
    public void findsElementsNonRecursiveSquareRoot() throws Throwable {
        assertFindsElementsInSample("findsElementsNonRecursiveSquareRoot",
                aPaginator.withSelector(KthElementSelector.class).withPartitioner(SquareRoot.class).makeSelector());
    }

    @Test
    public void findsPagesInPaginator() throws Throwable {
        long start = System.currentTimeMillis();
        sample.stream()
                .mapToInt(i -> i / pageSize)
                .forEach(
                        i -> {
                            Collections.shuffle(data);

                            List<Integer> paged = aPaginator.ofClass(DoubleSelectorPaginator.class).make().getPage(i, pageSize);
                            assertThat(paged.get(0) % pageSize).isZero();
                            assertThat(paged.size()).isLessThanOrEqualTo(pageSize);
                        });

        long end = System.currentTimeMillis();
        System.out.println("findsPagesInPaginator: " + (comparissons.get() / data.size() / sample.size())
                + " comparisson rate; " + ((end - start) / 1000) + " seconds");
    }

    @Test
    public void findsPagesInSortingPaginator() throws Throwable {
        long start = System.currentTimeMillis();
        sample.stream().mapToInt(i -> i / pageSize).forEach(i -> {
            Collections.shuffle(data);
            List<Integer> paged = aPaginator.ofClass(SortingPaginator.class).make().getPage(i, pageSize);
            assertThat(paged.get(0) % pageSize).isZero();
            assertThat(paged.size()).isLessThanOrEqualTo(pageSize);
        });

        long end = System.currentTimeMillis();
        System.out.println("findsPagesInSortingPaginator: " + (comparissons.get() / data.size() / sample.size())
                + " comparisson rate; " + ((end - start) / 1000) + " seconds");
    }

    @Test
    public void findsPagesInRecursivePaginator() throws Throwable {
        long start = System.currentTimeMillis();
        sample.stream()
                .mapToInt(i -> i / pageSize)
                .forEach(
                        i -> {
                            Collections.shuffle(data);
                            List<Integer> paged = aPaginator.make().getPage(i, pageSize);
                            assertThat(paged.get(0) % pageSize).isZero();
                            assertThat(paged.size()).isLessThanOrEqualTo(pageSize);
                        });

        long end = System.currentTimeMillis();
        System.out.println("findsPagesInRecursivePaginator: " + (comparissons.get() / data.size() / sample.size())
                + " comparisson rate; " + ((end - start) / 1000) + " seconds");
    }

    @Test
    public void findsPagesInPaginatorWithElementPositionSelector() throws Throwable {
        long start = System.currentTimeMillis();
        sample.stream()
                .mapToInt(i -> i / pageSize)
                .forEach(
                        i -> {
                            Collections.shuffle(data);

                            List<Integer> paged = aPaginator.ofClass(DoubleSelectorPaginator.class)
                                    .withSelector(ElementPositionSelector.class).make().getPage(i, pageSize);
                            assertThat(paged.get(0) % pageSize).isZero();
                            assertThat(paged.size()).isLessThanOrEqualTo(pageSize);
                        });
        long end = System.currentTimeMillis();
        System.out.println("findsPagesInPaginatorWithElementPositionSelector: " + (comparissons.get() / data.size() / sample.size())
                + " comparisson rate; " + ((end - start) / 1000) + " seconds");
    }

    @Test
    public void findsPagesInSortingPaginatorWithElementPositionSelector() throws Throwable {
        long start = System.currentTimeMillis();
        sample.stream().mapToInt(i -> i / pageSize).forEach(i -> {
            Collections.shuffle(data);
            List<Integer> paged = aPaginator.ofClass(SortingPaginator.class)
                    .withSelector(ElementPositionSelector.class).make().getPage(i, pageSize);
            assertThat(paged.get(0) % pageSize).isZero();
            assertThat(paged.size()).isLessThanOrEqualTo(pageSize);
        });
        long end = System.currentTimeMillis();
        System.out.println("findsPagesInSortingPaginatorWithElementPositionSelector: " + (comparissons.get() / data.size() / sample.size())
                + " comparisson rate; " + ((end - start) / 1000) + " seconds");
    }

    @Test
    public void findsPagesInRecursivePaginatorWithElementPositionSelector() throws Throwable {
        long start = System.currentTimeMillis();
        sample.stream()
                .mapToInt(i -> i / pageSize)
                .forEach(
                        i -> {
                            Collections.shuffle(data);
                            List<Integer> paged = aPaginator.withSelector(ElementPositionSelector.class).make().getPage(i, pageSize);
                            assertThat(paged.get(0) % pageSize).isZero();
                            assertThat(paged.size()).isLessThanOrEqualTo(pageSize);
                        });

        long end = System.currentTimeMillis();
        System.out.println("findsPagesInRecursivePaginatorWithElementPositionSelector: " + (comparissons.get() / data.size() / sample.size())
                + " comparisson rate; " + ((end - start) / 1000) + " seconds");
    }

    @Test
    public void findsElementsRecursiveLog2() throws Throwable {
        assertFindsElementsInSample("findsElementsRecursiveLog2", aPaginator.makeSelector());
    }

    @Test
    public void findsElementsRecursiveFixedSize20() throws Throwable {
        assertFindsElementsInSample("findsElementsRecursiveFixedSize20", aPaginator.withPartitioner(FixedSize.class).withFixedSize(20).makeSelector());
    }

    @Test
    public void findsElementsRecursiveFixedSize200() throws Throwable {
        assertFindsElementsInSample("findsElementsRecursiveFixedSize200", fixedSizeSelector(200));
    }

    @Test
    public void findsElementsRecursiveFixedSize2000() throws Throwable {
        assertFindsElementsInSample("findsElementsRecursiveFixedSize2000", fixedSizeSelector(2000));
    }

    @Test
    public void findsElementsRecursiveFixedSize600() throws Throwable {
        assertFindsElementsInSample("findsElementsRecursiveFixedSize600", fixedSizeSelector(600));
    }

    @Test
    public void findsElementsRecursiveFixedSize1000() throws Throwable {
        assertFindsElementsInSample("findsElementsRecursiveFixedSize1000", fixedSizeSelector(1000));
    }

    @Test
    public void findsElementsRecursiveFixedSize1500() throws Throwable {
        assertFindsElementsInSample("findsElementsRecursiveFixedSize1500", fixedSizeSelector(1500));
    }

    @Test
    public void findsElementsInElementPositionSelector() throws Throwable {
        assertFindsElementsInSample("findsElementsInElementPositionSelector", aPaginator.withSelector(ElementPositionSelector.class).makeSelector());
    }

    @Test
    public void findsElementsInParallelElementPositionSelector() throws Throwable {
        assertFindsElementsInSample("findsElementsInParallelElementPositionSelector", aPaginator.withSelector(ParallelElementPositionSelector.class).makeSelector());
    }

    protected Selector<Integer> fixedSizeSelector(final int fixedSize) throws Throwable {
        return aPaginator.withPartitioner(FixedSize.class).withFixedSize(fixedSize).makeSelector();
    }
}
