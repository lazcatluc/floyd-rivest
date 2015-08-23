package ro.contezi.floyd.rivest.partition;

import java.util.Collection;
import java.util.function.Function;

import ro.contezi.floyd.rivest.Partition;

public class FixedSize<T> implements Partition<T> {
    
    private final Collection<T> original;
    private final int fixedSize;

    public FixedSize(Collection<T> original, int fixedSize) {
        this.original = original;
        this.fixedSize = fixedSize;
    }

    @Override
    public Collection<T> partition() {
        if (original.size() <= fixedSize) {
            return original;
        }
        return new MyIndexPartition().partition();
    }

    private class MyIndexPartition extends Index<T> {

        public MyIndexPartition() {
            super(original);
        }

        @Override
        protected int desiredSize() {
            return fixedSize;
        }
        
    }
    
    public static <T> Function<Collection<T>, Partition<T>> supplier(int fixedSize) {
        return (original) -> new FixedSize<T>(original, fixedSize);
    }
}
