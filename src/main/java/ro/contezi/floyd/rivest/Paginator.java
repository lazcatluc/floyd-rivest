package ro.contezi.floyd.rivest;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Paginator<T> {

    private final Collection<T> data;
    private final PartitionSelector<T> selector;
    private final Comparator<? super T> comparator;

    public Paginator(Collection<T> data, PartitionSelector<T> selector,
            Comparator<? super T> comparator) {
        this.data = data;
        this.selector = selector;
        this.comparator = comparator;
    }

    public List<T> getPage(long page, long pageSize) {
        long firstResult = page * pageSize;
        long lastResult = (page + 1) * pageSize - 1;
        if (firstResult < 0) {
            firstResult = 0;
        }
        if (lastResult > data.size() - 1) {
            lastResult = data.size() - 1;
        }
        if (lastResult < firstResult) {
            return Collections.emptyList();
        }
        T first = selector.find(firstResult);
        T last = selector.find(lastResult);
        List<T> pageResult = data.parallelStream().filter(t -> comparator.compare(first, t) <= 0 && 
                comparator.compare(t, last) <= 0).collect(Collectors.toList());
        Collections.sort(pageResult, comparator);
        return pageResult;
    }
    
}
