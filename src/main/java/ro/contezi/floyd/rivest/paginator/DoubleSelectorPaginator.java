package ro.contezi.floyd.rivest.paginator;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import ro.contezi.floyd.rivest.Paginator;
import ro.contezi.floyd.rivest.Selector;

public class DoubleSelectorPaginator<T> implements Paginator<T> {

    private final Collection<T> data;
    private final Selector<T> selector;
    private final Comparator<? super T> comparator;

    public DoubleSelectorPaginator(Collection<T> data, Selector<T> selector,
            Comparator<? super T> comparator) {
        this.data = data;
        this.selector = selector;
        this.comparator = comparator;
    }

    @Override
    public List<T> getBetween(long firstResult, long lastResult) {
        T first = selector.find(firstResult);
        T last = selector.find(lastResult);
        List<T> pageResult = data.parallelStream().filter(t -> comparator.compare(first, t) <= 0 && 
                comparator.compare(t, last) <= 0).collect(Collectors.toList());
        Collections.sort(pageResult, comparator);
        return pageResult;
    }

    @Override
    public long getTotalResults() {
        return data.size();
    }
    
}
