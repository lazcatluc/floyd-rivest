package ro.contezi.floyd.rivest.paginator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ro.contezi.floyd.rivest.Paginator;

public class SortingPaginator<T> implements Paginator<T> {
    
    private final List<T> data;

    public SortingPaginator(List<T> data, Comparator<? super T> comparator) {
        this.data = data;
        Collections.sort(this.data, comparator);
    }

    @Override
    public List<T> getBetween(long first, long last) {
        return data.subList((int)first, (int)last + 1);
    }

    @Override
    public long getTotalResults() {
        return data.size();
    }

}
