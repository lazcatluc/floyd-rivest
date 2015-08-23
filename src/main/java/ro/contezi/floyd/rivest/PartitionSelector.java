package ro.contezi.floyd.rivest;

public interface PartitionSelector<T> {

    /**
     * 
     * @param k
     *            the rank of the element to find
     * @return the kth element
     */
    public abstract T find(long k);

    /**
     * 
     * @param k
     *            the rank of the element to find
     * @return the rank in the smaller partition
     */
    public abstract int partitionRank(long k);

}