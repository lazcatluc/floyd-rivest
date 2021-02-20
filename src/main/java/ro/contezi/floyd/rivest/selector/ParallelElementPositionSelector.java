package ro.contezi.floyd.rivest.selector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import ro.contezi.floyd.rivest.ElementPosition;
import ro.contezi.floyd.rivest.Selector;

public class ParallelElementPositionSelector<T> implements Selector<T> {

    private final List<ElementPosition<T>> elementPositions;

    public ParallelElementPositionSelector(Collection<T> original, Comparator<? super T> comparator, int parallelism) {
        elementPositions = new ArrayList<>(parallelism);
        List<T> originalList = new ArrayList<>(original);
        IntStream.range(0, original.size() / parallelism + 1).forEach(i -> elementPositions.add(
                ElementPosition.in(originalList.subList(i * parallelism,
                        Math.min(originalList.size(), (i + 1) * parallelism)), comparator)));
    }

    @Override
    public T find(long k) {
        elementPositions.parallelStream().forEach(ElementPosition::reset);
        int rankSum;
        T pivot = elementPositions.iterator().next().first();
        do {
            position(pivot);
            rankSum = elementPositions.parallelStream().mapToInt(ElementPosition::getPositionedRank).sum();
            if (rankSum == k) {
                return pivot;
            }
            if (k < rankSum) {
                pivot = getOneLower();
            } else {
                pivot = getOneHigher();
                k -= rankSum;
            }
        } while (true);
    }

    private T getOneHigher() {
        return elementPositions.parallelStream().map(ElementPosition::getOneHigherOptional)
                .filter(Optional::isPresent).map(Optional::get).findAny().orElseThrow(ArrayIndexOutOfBoundsException::new);
    }

    private T getOneLower() {
        return elementPositions.parallelStream().map(ElementPosition::getOneLowerOptional)
                .filter(Optional::isPresent).map(Optional::get).findAny().orElseThrow(ArrayIndexOutOfBoundsException::new);
    }

    private void position(T pivot) {
        elementPositions.parallelStream().forEach(ep -> ep.position(pivot));
    }
}
