package ro.contezi.floyd.rivest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public abstract class IndexPartition<T> implements Partition<T> {

    private final Collection<T> original;

    protected abstract int desiredSize();

    protected final Random random;

    public IndexPartition(Collection<T> original) {
        this.original = original;
        random = new Random();
    }

    @Override
    public Collection<T> partition() {
        int[] indicesToTakeIntoPartition = getIndicesToTakeIntoPartition();
    
        Collection<T> ret = new ArrayList<>();
        int originalCounter = 0;
        int indicesToTakeIntoPartitionCounter = 0;
        for (T t : original) {
            while (indicesToTakeIntoPartitionCounter < indicesToTakeIntoPartition.length
                    && originalCounter == indicesToTakeIntoPartition[indicesToTakeIntoPartitionCounter]) {
                ret.add(t);
                indicesToTakeIntoPartitionCounter++;
            }
            originalCounter++;
        }
        return ret;
    }

    protected int[] getIndicesToTakeIntoPartition() {
        int[] indicesToTakeIntoPartition = new int[desiredSize()];
        for (int i = 0; i < indicesToTakeIntoPartition.length; i++) {
            indicesToTakeIntoPartition[i] = random.nextInt(original.size());
        }
        Arrays.sort(indicesToTakeIntoPartition);
        return indicesToTakeIntoPartition;
    }

    protected int originalSize() {
        return original.size();
    }

}