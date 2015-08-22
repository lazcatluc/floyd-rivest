package ro.contezi.floyd.rivest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public class SquareRootPartition {
    
    public <T> Collection<T> partition(Collection<T> original) {
        int size = original.size();
        int desiredSize = (int) Math.sqrt(size);
        
        int[] indicesToTakeIntoPartition = new int[desiredSize];
        Random random = new Random();
        for (int i = 0; i < indicesToTakeIntoPartition.length; i++) {
            indicesToTakeIntoPartition[i] = random.nextInt(size);
        }
        Arrays.sort(indicesToTakeIntoPartition);
        
        Collection<T> ret = new ArrayList<>();
        int i = 0;
        int j = 0;
        for (T t : original) {
            while (j < indicesToTakeIntoPartition.length && i == indicesToTakeIntoPartition[j]) {
                ret.add(t);
                j++;
            }
            i++;
        }
        return ret;
    }
    
}
