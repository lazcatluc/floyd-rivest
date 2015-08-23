package ro.contezi.floyd.rivest;

public interface Selector<T> {
    /**
     * 
     * @param k
     *            the rank of the element to find
     * @return the kth element
     */
    T find(long k);
}
