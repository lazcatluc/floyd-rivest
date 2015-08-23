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

public class ElementSelectorITest {
    
    private List<Integer> data;
    private List<Integer> sample;
    private AtomicLong comparissons;
    private Comparator<Integer> comparator;
    
    @Before
    public void setUp() {
        data = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            data.add(i);
        }
        Collections.shuffle(data);
        sample = data.subList(0, 100);
        comparissons = new AtomicLong(0);
        comparator = (e1, e2) ->  {comparissons.incrementAndGet(); return e1.compareTo(e2);};

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
        assertFindsElementsInSample(new KthElementSelector<>(data, new SquareRootPartition<>(data),
                comparator));
    }

    @Test
    public void findsPagesInPaginator() throws Exception {
        Paginator<Integer> paginator = new Paginator<>(data, new RecursiveKthElementSelector<Integer>(data, Log2Partition<Integer>::new,
                comparator), comparator);
        int pageSize = 10;
        sample.stream().mapToInt(i -> i / pageSize).forEach(i -> paginator.getPage(i, pageSize));
        
        System.out.println(comparissons.get() / data.size() / sample.size());
    }
    
    @Test
    public void findsElementsRecursiveLog2() throws Exception {
        assertFindsElementsInSample(new RecursiveKthElementSelector<Integer>(data, Log2Partition<Integer>::new,
                comparator));
    }
    
    @Test
    public void findsElementsRecursiveFixedSize20() throws Exception {
        assertFindsElementsInSample(new RecursiveKthElementSelector<Integer>(data, FixedSizePartition.supplier(20),
                comparator));
    }
    
    @Test
    public void findsElementsRecursiveFixedSize200() throws Exception {
        assertFindsElementsInSample(new RecursiveKthElementSelector<Integer>(data, FixedSizePartition.supplier(200),
                comparator));
    }
    
    @Test
    public void findsElementsRecursiveFixedSize2000() throws Exception {
        assertFindsElementsInSample(new RecursiveKthElementSelector<Integer>(data, FixedSizePartition.supplier(2000),
                comparator));
    }
    
    @Test
    public void findsElementsRecursiveFixedSize600() throws Exception {
        assertFindsElementsInSample(new RecursiveKthElementSelector<Integer>(data, FixedSizePartition.supplier(600),
                comparator));
    }
    
    @Test
    public void findsElementsRecursiveFixedSize1000() throws Exception {
        assertFindsElementsInSample(new RecursiveKthElementSelector<Integer>(data, FixedSizePartition.supplier(1000),
                comparator));
    }
    
    @Test
    public void findsElementsRecursiveFixedSize1500() throws Exception {
        assertFindsElementsInSample(new RecursiveKthElementSelector<Integer>(data, FixedSizePartition.supplier(1500),
                comparator));
    }
}
