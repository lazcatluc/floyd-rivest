package ro.contezi.floyd.rivest;

import java.util.Collections;
import java.util.List;

public interface Paginator<T> {
    default List<T> getPage(long page, long pageSize) {
        long firstResult = page * pageSize;
        long lastResult = (page + 1) * pageSize - 1;
        if (firstResult < 0) {
            firstResult = 0;
        }
        if (lastResult > getTotalResults() - 1) {
            lastResult = getTotalResults() - 1;
        }
        if (lastResult < firstResult) {
            return Collections.emptyList();
        }
        return getBetween(firstResult, lastResult);
    }
    List<T> getBetween(long first, long last);
    long getTotalResults();
}
