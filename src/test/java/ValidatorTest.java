import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidatorTest {
    @Test
    void nicknameValidationTest() {
        assertThat(Pattern.matches("^[가-힣]{1,10}$", "")).isEqualTo(false);
        assertThat(Pattern.matches("^[가-힣]{1,10}$", "ㅎ")).isEqualTo(false);
        assertThat(Pattern.matches("^[가-힣]{1,10}$", "히")).isEqualTo(true);
        assertThat(Pattern.matches("^[가-힣]{1,10}$", "안녕세상아")).isEqualTo(true);
        assertThat(Pattern.matches("^[가-힣]{1,10}$", "안녕 세상아")).isEqualTo(false);
        assertThat(Pattern.matches("^[가-힣]{1,10}$", "HelloWorld")).isEqualTo(false);
        assertThat(Pattern.matches("^[가-힣]{1,10}$", "안녕세상아!")).isEqualTo(false);
        assertThat(Pattern.matches("^[가-힣]{1,10}$", "안녕세상아안녕세상아")).isEqualTo(true);
        assertThat(Pattern.matches("^[가-힣]{1,10}$", "안녕세상아안녕세상아안녕세상아안녕세상아안")).isEqualTo(false);
    }

}
