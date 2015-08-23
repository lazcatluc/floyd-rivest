package ro.contezi.floyd.rivest.selector;

import ro.contezi.floyd.rivest.Selector;

public interface PartitionSelector<T> extends Selector<T> {

    /**
     * 
     * @param k
     *            the rank of the element to find
     * @return the rank in the smaller partition
     */
    int partitionRank(long k);

}