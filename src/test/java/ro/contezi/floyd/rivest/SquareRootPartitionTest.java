package ro.contezi.floyd.rivest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.Test;

import ro.contezi.floyd.rivest.partition.SquareRoot;

public class SquareRootPartitionTest extends PartitionTest {

    @Override
    protected <T> Collection<T> partition(Collection<T> original) {
        return new SquareRoot<>(original).partition();
    }
    
    @Test
    public void partitionsOriginalIntoFiveElements() throws Exception {
        assertThat(new SquareRoot<>(ORIGINAL).partition()).hasSize(5);
    }

}
