package ro.contezi.floyd.rivest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RecursiveKthElementSelector<T> implements PartitionSelector<T> {

    private final Collection<T> original;
    private final Comparator<? super T> comparator;
    private final List<T> partition;
    private final Function<Collection<T>, Partition<T>> partitionSupplier;
    private final double partitionRatio;

    public RecursiveKthElementSelector(Collection<T> original, Function<Collection<T>, Partition<T>> partitionSupplier,
            Comparator<? super T> comparator) {
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

    @Override
    public T find(long k) {
        int partitionRank = partitionRank(k);
        int lowerRank = partitionRank;
        int upperRank = partitionRank < partition.size() - 1 ? partitionRank + 1 : partitionRank;
        final T lowerBound = partition.get(lowerRank);
        long lowerBoundPosition = ElementPosition.in(original, comparator).of(lowerBound);
        if (lowerBoundPosition == k) {
            return lowerBound;
        }
        if (lowerBoundPosition > k) {
            List<T> newList = original.parallelStream().filter(t -> comparator.compare(t, lowerBound) < 0)
                    .collect(Collectors.toList());
            return new RecursiveKthElementSelector<T>(newList, partitionSupplier, comparator).find(k);
        }
        final T upperBound = partition.get(upperRank);
        long upperBoundPosition = ElementPosition.in(original, comparator).of(upperBound);
        if (upperBoundPosition == k) {
            return upperBound;
        }
        if (upperBoundPosition < k) {
            List<T> newList = original.parallelStream().filter(t -> comparator.compare(upperBound, t) < 0)
                    .collect(Collectors.toList());
            return new RecursiveKthElementSelector<T>(newList, partitionSupplier, comparator).find(k
                    - upperBoundPosition - 1);
        }

        List<T> newList = original.parallelStream()
                .filter(t -> comparator.compare(upperBound, t) > 0 && comparator.compare(t, lowerBound) > 0)
                .collect(Collectors.toList());
        return new RecursiveKthElementSelector<T>(newList, partitionSupplier, comparator).find(k - lowerBoundPosition
                - 1);

    }

    @Override
    public int partitionRank(long k) {
        return Double.valueOf(k * partitionRatio).intValue();
    }

}
