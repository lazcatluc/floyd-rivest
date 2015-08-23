package ro.contezi.floyd.rivest.selector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import ro.contezi.floyd.rivest.ElementPosition;
import ro.contezi.floyd.rivest.Partition;

public class KthElementSelector<T> implements PartitionSelector<T> {
    private final Collection<T> original;
    private final List<T> partition;
    private final Comparator<? super T> comparator;
    private final double partitionRatio;
    private int lowerRank;
    private int upperRank;

    public KthElementSelector(Collection<T> original, Partition<T> partitioner, Comparator<? super T> comparator) {
        this.original = original;
        this.comparator = comparator;

        partition = new ArrayList<>(partitioner.partition());
        partition.add(original.stream().min(comparator).get());
        partition.add(original.stream().max(comparator).get());
        Collections.sort(partition, comparator);

        partitionRatio = 1.0 * partition.size() / original.size();
    }

    /* (non-Javadoc)
     * @see ro.contezi.floyd.rivest.ElementSelector#find(long)
     */
    @Override
    public T find(long k) {
        int partitionRank = partitionRank(k);
        lowerRank = partitionRank;
        upperRank = partitionRank == partition.size() - 1 ? partitionRank : partitionRank + 1;

        if (k < original.size() / 2) {
            return findLowerBoundFirst(k);
        }
        return findUpperBoundFirst(k);
    }
    
    private T findLowerBoundFirst(long k) {
        findLowerBoundPosition(k);
        findUpperBoundPosition(k);

        T lowerBound = partition.get(lowerRank);
        T upperBound = partition.get(upperRank);
        
        List<T> result = original.parallelStream()
                .filter(t -> comparator.compare(lowerBound, t) <= 0 && comparator.compare(t, upperBound) <= 0)
                .collect(Collectors.toList());
        Collections.sort(result, comparator);
        
        return result.get((int)(k - ElementPosition.in(original, comparator).of(lowerBound)));
    }
    
    private T findUpperBoundFirst(long k) {
        findUpperBoundPosition(k);
        findLowerBoundPosition(k);

        T upperBound = partition.get(upperRank);
        T lowerBound = partition.get(lowerRank);
        
        List<T> result = original.parallelStream()
                .filter(t -> comparator.compare(t, upperBound) <= 0 && comparator.compare(lowerBound, t) <= 0)
                .collect(Collectors.toList());
        Collections.sort(result, comparator);
        
        return result.get((int)(k - ElementPosition.in(original, comparator).of(lowerBound)));
    }

    protected long findUpperBoundPosition(long k) {
        long upperBoundPosition;
        do {
            upperBoundPosition = ElementPosition.in(original, comparator).of(partition.get(upperRank));
            if (upperBoundPosition < k) {
                lowerRank = upperRank;
                upperRank++;
            }
        } while (upperBoundPosition < k);
        return upperBoundPosition;
    }

    protected long findLowerBoundPosition(long k) {
        long lowerBoundPosition;
        do {
            lowerBoundPosition = ElementPosition.in(original, comparator).of(partition.get(lowerRank));
            if (lowerBoundPosition > k) {
                upperRank = lowerRank;
                lowerRank--;
            }
        } while (lowerBoundPosition > k);
        return lowerBoundPosition;
    }

    /* (non-Javadoc)
     * @see ro.contezi.floyd.rivest.ElementSelector#partitionRank(long)
     */
    @Override
    public int partitionRank(long k) {
        return Double.valueOf(k * partitionRatio).intValue();
    }
}
