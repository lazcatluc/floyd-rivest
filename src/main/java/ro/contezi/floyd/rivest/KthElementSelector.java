package ro.contezi.floyd.rivest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KthElementSelector<T> {
    private final Collection<T> original;
    private final List<T> partition;
    private final Comparator<? super T> comparator;
    private final double partitionRatio; 

    public KthElementSelector(Collection<T> original, Partition<T> partitioner, Comparator<? super T> comparator) {
        this.original = original;        
        this.comparator = comparator;
        
        partition = new ArrayList<>(partitioner.partition());
        Collections.sort(partition, comparator);
        
        partitionRatio = 1.0 * partition.size() / original.size();
    }

    /**
     * 
     * @param k the rank of the element to find
     * @return the kth element
     */
    public T find(int k) {
        int partitionRank = partitionRank(k);
        return null;
    }

    /**
     * 
     * @param k the rank of the element to find
     * @return the rank in the smaller partition
     */
    public int partitionRank(int k) {
        return Double.valueOf(k * partitionRatio).intValue();
    }
}
