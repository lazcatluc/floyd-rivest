package ro.contezi.floyd.rivest;

import java.util.Collection;
import java.util.Comparator;

public class ElementPosition<T> {

    private final Collection<T> elements;
    private final Comparator<? super T> comparator;

    private ElementPosition(Collection<T> elements, Comparator<? super T> comparator) {
        this.elements = elements;
        this.comparator = comparator;
    }
    
    public long of(T element) {
        return elements.stream().filter(e -> comparator.compare(e, element) < 0).count();
    }

    public static <T> ElementPosition<T> in(Collection<T> elements, Comparator<? super T> comparator) {
        return new ElementPosition<>(elements, comparator);
    }
 
    public static <T extends Comparable<? super T>> ElementPosition<T> in(Collection<T> elements) {
        return new ElementPosition<>(elements, (e1, e2) -> e1.compareTo(e2));
    }
}
