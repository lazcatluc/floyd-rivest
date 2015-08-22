package ro.contezi.floyd.rivest;

import java.util.Collection;

public class SquareRootPartition<T> extends IndexPartition<T> implements Partition<T> {

    public SquareRootPartition(Collection<T> original) {
        super(original);
    }

    @Override
    protected int desiredSize() {
        return (int) Math.sqrt(originalSize());
    }

}
