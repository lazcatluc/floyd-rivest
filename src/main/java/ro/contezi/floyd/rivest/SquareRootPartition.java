package ro.contezi.floyd.rivest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public class SquareRootPartition<T> {

    private final Collection<T> original;
    private final Random random;

    public SquareRootPartition(Collection<T> original) {
        this.original = original;
        random = new Random();
    }

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

    protected int desiredSize() {
        return (int) Math.sqrt(original.size());
    }

}
