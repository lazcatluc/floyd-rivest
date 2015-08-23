package ro.contezi.floyd.rivest.paginator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import ro.contezi.floyd.rivest.APaginator;
import ro.contezi.floyd.rivest.ElementPosition;
import ro.contezi.floyd.rivest.Paginator;
import ro.contezi.floyd.rivest.Partition;
import ro.contezi.floyd.rivest.partition.FixedSize;

public class RecursiveKthElementPaginator<T> implements Paginator<T> {

    private final Collection<T> original;
    private final Comparator<? super T> comparator;
    private final List<T> partition;
    private final Function<Collection<T>, Partition<T>> partitionSupplier;
    private final double partitionRatio;

    public RecursiveKthElementPaginator(Collection<T> original,
            Function<Collection<T>, Partition<T>> partitionSupplier, Comparator<? super T> comparator) {
        this.original = original;
        this.comparator = comparator;
        this.partitionSupplier = partitionSupplier;

        List<T> localPartition = new ArrayList<>(partitionSupplier.apply(original).partition());
        if (localPartition.isEmpty()) {
            localPartition = new ArrayList<>(original);
        }
        partition = localPartition;
        Collections.sort(partition, comparator);

        partitionRatio = 1.0 * partition.size() / original.size();
    }

    public int partitionRank(long k) {
        return Double.valueOf(k * partitionRatio).intValue();
    }

    @Override
    public List<T> getBetween(long firstResult, long lastResult) {
        if (original.isEmpty()) {
            return Collections.emptyList();
        }
        int lowerRank = partitionRank(firstResult);
        int upperRank = partitionRank(lastResult);
        if (upperRank == lowerRank && upperRank < partition.size() - 1) {
            upperRank++;
        }

        final T lowerBound = partition.get(lowerRank);
        long lowerBoundPosition = ElementPosition.in(original, comparator).of(lowerBound);
        if (lowerBoundPosition > lastResult) {
            return newListPaginator(
                    original.parallelStream().filter(t -> comparator.compare(t, lowerBound) < 0)
                            .collect(Collectors.toList())).getBetween(firstResult, lastResult);
        }

        final T upperBound = partition.get(upperRank);
        long upperBoundPosition = ElementPosition.in(original, comparator).of(upperBound);
        if (upperBoundPosition < firstResult) {
            return newListPaginator(
                    original.parallelStream().filter(t -> comparator.compare(upperBound, t) < 0)
                            .collect(Collectors.toList())).getBetween(firstResult - upperBoundPosition - 1,
                    lastResult - upperBoundPosition - 1);
        }

        if (lowerBoundPosition < firstResult && upperBoundPosition > lastResult) {
            return newListPaginator(
                    original.parallelStream()
                            .filter(t -> comparator.compare(lowerBound, t) < 0 && comparator.compare(t, upperBound) < 0)
                            .collect(Collectors.toList())).getBetween(firstResult - lowerBoundPosition - 1,
                    lastResult - lowerBoundPosition - 1);
        }

        return myPaginator().withData(original).ofClass(DoubleSelectorPaginator.class).withPartitioner(FixedSize.class)
                .make().getBetween(firstResult, lastResult);
    }

    protected Paginator<T> newListPaginator(List<T> newList) {
        return myPaginator().withData(newList).make();
    }

    protected APaginator<T> myPaginator() {
        return APaginator.<T> of(comparator).withPartitionSupplier(partitionSupplier);
    }

    @Override
    public long getTotalResults() {
        return original.size();
    }

}
