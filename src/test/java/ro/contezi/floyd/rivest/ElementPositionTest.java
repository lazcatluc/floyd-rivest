package ro.contezi.floyd.rivest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ElementPositionTest {
    private List<Integer> numbers = Arrays.asList(5, 0, 9, 10, 2, 6, 3, 4, 1, 8);

    @Test
    public void finds4ToBeThe5thElementInNaturalNumbers() {
        assertThat(ElementPosition.in(numbers).of(4)).isEqualTo(4);
    }

    @Test
    public void positions4AsThe5thElementInNaturalNumbers() {
        assertThat(ElementPosition.in(numbers).position(4).getPositionedRank()).isEqualTo(4);
    }

    @Test
    public void positions6AsThe2ndElementInNaturalNumbersAfterHavingPositioned4() {
        assertThat(ElementPosition.in(numbers).position(4).position(6).getPositionedRank()).isEqualTo(2);
    }

    @Test
    public void positions5AsThe1stElementInNaturalNumbersAfterHavingPositioned4And6() {
        assertThat(ElementPosition.in(numbers).position(4).position(6).position(5).getPositionedRank()).isEqualTo(1);
    }
}
