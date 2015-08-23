package ro.contezi.floyd.rivest;

import java.util.List;

public interface Paginator<T> {
    public List<T> getPage(long page, long pageSize);
}
