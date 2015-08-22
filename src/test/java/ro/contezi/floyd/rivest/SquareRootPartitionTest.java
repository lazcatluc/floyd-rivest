package ro.contezi.floyd.rivest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.Test;

public class SquareRootPartitionTest extends PartitionTest {

    @Override
    protected <T> Collection<T> partition(Collection<T> original) {
        return new SquareRootPartition().partition(original);
    }
    
    @Test
    public void partitionsOriginalIntoFourElements() throws Exception {
        assertThat(new SquareRootPartition().partition(ORIGINAL)).hasSize(4);
    }

}