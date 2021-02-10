package ro.contezi.floyd.rivest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class ElementPosition<T> {

    private final Collection<T> elements;
    private final Comparator<? super T> comparator;
    private List<T> lower;
    private List<T> higherOrEqual;
    private T positioned;

    private ElementPosition(Collection<T> elements, Comparator<? super T> comparator) {
        this.elements = elements;
        this.comparator = comparator;
    }
    
    public long of(T element) {
        return elements.parallelStream().filter(e -> comparator.compare(e, element) < 0).count();
    }

    public ElementPosition<T> position(T element) {
        if (positioned == null) {
            lower = new ArrayList<>();
            higherOrEqual = new ArrayList<>();
            elements.forEach(e -> {
                if (comparator.compare(e, element) < 0) {
                    lower.add(e);
                } else {
                    higherOrEqual.add(e);
                }
            });
        } else if (comparator.compare(element, positioned) < 0) {
            List<T> newLower = new ArrayList<>();
            higherOrEqual = new ArrayList<>();
            lower.forEach(e -> {
                if (comparator.compare(e, element) < 0) {
                    newLower.add(e);
                } else {
                    higherOrEqual.add(e);
                }
            });
            lower = newLower;
        } else {
            lower = new ArrayList<>();
            List<T> newHigherOrEqual = new ArrayList<>();
            higherOrEqual.forEach(e -> {
                if (comparator.compare(e, element) < 0) {
                    lower.add(e);
                } else {
                    newHigherOrEqual.add(e);
                }
            });
            higherOrEqual = newHigherOrEqual;
        }
        positioned = element;
        return this;
    }

    public int getPositionedRank() {
        return lower.size();
    }

    public static <T> ElementPosition<T> in(Collection<T> elements, Comparator<? super T> comparator) {
        return new ElementPosition<>(elements, comparator);
    }
 
    public static <T extends Comparable<? super T>> ElementPosition<T> in(Collection<T> elements) {
        return new ElementPosition<>(elements, Comparator.naturalOrder());
    }
}
