package ro.contezi.floyd.rivest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.Test;

public class Log2PartitionTest extends PartitionTest {
    @Override
    protected <T> Collection<T> partition(Collection<T> original) {
        return new Log2Partition<>(original).partition();
    }
    
    @Test
    public void partitionsOriginalIntoFourElements() throws Exception {
        assertThat(new Log2Partition<>(ORIGINAL).partition()).hasSize(4);
    }
}
