package ro.contezi.floyd.rivest;

import java.util.Collection;

public class Log2Partition<T> extends IndexPartition<T> {
    
    private static final Double LOG2 = Math.log(2);

    public Log2Partition(Collection<T> original) {
        super(original);
    }

    @Override
    protected int desiredSize() {
        return (int)(Math.log(originalSize())/LOG2);
    }

}
