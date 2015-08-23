package ro.contezi.floyd.rivest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class PartitionTest {
    static final List<Integer> ORIGINAL = 
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25);
    
    @Test
    public void shouldPartitionIntoElementsOfTheSameCollection() throws Exception {
        
        assertThat(ORIGINAL).containsAll(partition(ORIGINAL));
    }

    protected <T> Collection<T> partition(Collection<T> original) {
        return original;
    }
}
