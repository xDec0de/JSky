import net.codersky.jsky.strings.JStrings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJStrings {

	@Test
	public void testStringHasContent() {
		Assertions.assertFalse(JStrings.hasContent(null));
		Assertions.assertFalse(JStrings.hasContent(""));
		Assertions.assertFalse(JStrings.hasContent("\t"));
		Assertions.assertTrue(JStrings.hasContent("Hello"));
	}
}
