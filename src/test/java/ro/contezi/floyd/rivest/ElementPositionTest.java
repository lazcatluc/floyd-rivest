package ro.contezi.floyd.rivest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ElementPositionTest {
    @Test
    public void finds4ToBeThe4thElementInNaturalNumbers() throws Exception {
        List<Integer> numbers = Arrays.asList(5, 0, 9, 10, 2, 6, 3, 4, 1, 8);
        
        assertThat(new ElementPosition<>(numbers).getPositionFor(4)).isEqualTo(4);
    }
}
