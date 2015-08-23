package ro.contezi.floyd.rivest.partition;

import java.util.Collection;

import ro.contezi.floyd.rivest.Partition;

public class SquareRoot<T> extends Index<T> implements Partition<T> {

    public SquareRoot(Collection<T> original) {
        super(original);
    }

    @Override
    protected int desiredSize() {
        return (int) Math.sqrt(originalSize());
    }

}
