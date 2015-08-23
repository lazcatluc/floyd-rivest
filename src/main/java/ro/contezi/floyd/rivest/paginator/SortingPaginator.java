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
        return data.subList((int)firstResult, (int)lastResult + 1);
    }

}
