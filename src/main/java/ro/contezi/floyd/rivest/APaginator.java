package ro.contezi.floyd.rivest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import ro.contezi.floyd.rivest.paginator.DoubleSelectorPaginator;
import ro.contezi.floyd.rivest.paginator.RecursiveKthElementPaginator;
import ro.contezi.floyd.rivest.paginator.SortingPaginator;
import ro.contezi.floyd.rivest.partition.FixedSize;
import ro.contezi.floyd.rivest.partition.Log2;
import ro.contezi.floyd.rivest.partition.SquareRoot;
import ro.contezi.floyd.rivest.selector.ElementPositionSelector;
import ro.contezi.floyd.rivest.selector.KthElementSelector;
import ro.contezi.floyd.rivest.selector.ParallelElementPositionSelector;
import ro.contezi.floyd.rivest.selector.RecursiveKthElementSelector;

@SuppressWarnings("rawtypes")
public class APaginator<T> {
    private Collection<T> data = Collections.emptyList();
    private Comparator<? super T> comparator;
    private Map<Class<? extends Partition>, Function<Collection<T>, Partition<T>>> partitioners = new HashMap<>();
    private Map<Class<? extends Selector>, Supplier<Selector<T>>> selectors = new HashMap<>();
    private Map<Class<? extends Paginator>, Supplier<Paginator<T>>> paginators = new HashMap<>();
    private Class<? extends Selector> selector = RecursiveKthElementSelector.class;
    private Class<? extends Partition> partition = Log2.class;
    private Class<? extends Paginator> paginator = RecursiveKthElementPaginator.class;
    private int fixedSize = 200;
    
    private APaginator() {
        partitioners.put(Log2.class, Log2<T>::new);
        partitioners.put(SquareRoot.class, SquareRoot<T>::new);
        partitioners.put(FixedSize.class, (data) -> new FixedSize<>(data, fixedSize));
        selectors.put(RecursiveKthElementSelector.class,
                () -> new RecursiveKthElementSelector<T>(data, partitioners.get(partition), comparator));
        selectors.put(ElementPositionSelector.class, () -> new ElementPositionSelector<>(data, comparator));
        selectors.put(KthElementSelector.class, () -> new KthElementSelector<T>(data, partitioners.get(partition)
                .apply(data), comparator));
        selectors.put(ParallelElementPositionSelector.class, () -> new ParallelElementPositionSelector<>(data, comparator, 8));
        paginators.put(DoubleSelectorPaginator.class, () -> new DoubleSelectorPaginator<>(data, selectors.get(selector)
                .get(), comparator));
        paginators.put(SortingPaginator.class, () -> new SortingPaginator<>(new ArrayList<>(data), comparator));
        paginators.put(RecursiveKthElementPaginator.class,
                () -> new RecursiveKthElementPaginator<>(data, partitioners.get(partition), comparator));  
    }

    public APaginator<T> withData(Collection<T> data) {
        this.data = data;
        return this;
    }

    public APaginator<T> withComparator(Comparator<? super T> comparator) {
        this.comparator = comparator;
        return this;
    }

    public APaginator<T> withPartitioner(Class<? extends Partition> partition) {
        this.partition = partition;
        return this;
    }

    public APaginator<T> withFixedSize(int fixedSize) {
        this.fixedSize = fixedSize;
        return this;
    }

    public APaginator<T> withSelector(Class<? extends Selector> selector) {
        this.selector = selector;
        return this;
    }

    public APaginator<T> ofClass(Class<? extends Paginator> paginator) {
        this.paginator = paginator;
        return this;
    }

    public Paginator<T> make() {
        return paginators.get(paginator).get();
    }
    
    public Selector<T> makeSelector() {
        return selectors.get(selector).get();
    }
    
    public Partition<T> makePartition() {
        return partitioners.get(partition).apply(data);
    }

    public APaginator<T> withPartition(Partition<T> partition2) {
        partitioners.put(partition2.getClass(), (data) -> partition2);
        partition = partition2.getClass();
        return this;
    }
    
    public APaginator<T> withPartitionSupplier(Function<Collection<T>, Partition<T>> partitionSupplier) {
        partitioners.put(Partition.class, partitionSupplier);
        partition = Partition.class;
        return this;
    }
    
    public static <T extends Comparable<T>> APaginator<T> of(Class<T> clazz) {
        return new APaginator<T>().withComparator((t1, t2) -> t1.compareTo(t2));
    }
    
    public static <T> APaginator<T> of(Comparator<? super T> comp) {
        return new APaginator<T>().withComparator((t1, t2) -> comp.compare(t1, t2));
    }

}
