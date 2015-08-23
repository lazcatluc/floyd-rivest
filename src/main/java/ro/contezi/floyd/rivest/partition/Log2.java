package ro.contezi.floyd.rivest.partition;

import java.util.Collection;

public class Log2<T> extends Index<T> {
    
    private static final Double LOG2 = Math.log(2);

    public Log2(Collection<T> original) {
        super(original);
    }

    @Override
    protected int desiredSize() {
        final int originalSize = originalSize();
        if (originalSize < 2) {
            return 0;
        }
        return (int)(Math.log(originalSize)/LOG2);
    }

}
