package ro.contezi.floyd.rivest.selector;

import java.util.Collection;
import java.util.Comparator;
import ro.contezi.floyd.rivest.ElementPosition;
import ro.contezi.floyd.rivest.Selector;

public class ElementPositionSelector<T> implements Selector<T> {

    private final Collection<T> original;
    private final Comparator<? super T> comparator;

    public ElementPositionSelector(Collection<T> original, Comparator<? super T> comparator) {
        this.original = original;
        this.comparator = comparator;
    }

    @Override
    public T find(long k) {
        ElementPosition<T> elementPosition = ElementPosition.in(original, comparator);
        T pivot = original.iterator().next();
        elementPosition.position(pivot);
        while (k != elementPosition.getPositionedRank()) {
            T newPivot;
            if (k < elementPosition.getPositionedRank()) {
                newPivot = elementPosition.getOneLower();
            } else {
                newPivot = elementPosition.getOneHigher();
                k -= elementPosition.getPositionedRank();
            }
            if (newPivot == null) {
                return pivot;
            }
            pivot = newPivot;
            elementPosition.position(pivot);
        }
        return pivot;
    }
}
