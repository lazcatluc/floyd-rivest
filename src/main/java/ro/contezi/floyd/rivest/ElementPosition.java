package ro.contezi.floyd.rivest;

import java.util.Collection;
import java.util.Comparator;

public class ElementPosition<T> {

    private final Collection<T> elements;
    private final Comparator<? super T> comparator;

    public ElementPosition(Collection<T> elements, Comparator<? super T> comparator) {
        this.elements = elements;
        this.comparator = comparator;
    }
    
    @SuppressWarnings("unchecked")
    public ElementPosition(Collection<T> elements) {
        this(elements, (e1, e2) -> ((Comparable<? super T>)e1).compareTo(e2));
    }

    public long getPositionFor(T element) {
        return elements.stream().filter(e -> comparator.compare(e, element) < 0).count();
    }

}
