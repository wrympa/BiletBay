package BB;

import org.testng.annotations.Test;

import javax.validation.constraints.AssertTrue;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.testng.AssertJUnit.assertEquals;

public class TestReport {
    @Test
    public void basicTest() {
        Report report = new Report("snitch", "witch", "she douth soldeth me a concoction not a potion");
        assertEquals("snitch", report.getReporter());
        assertEquals("witch", report.getReported());
        assertEquals("she douth soldeth me a concoction not a potion", report.getReason());
    }
    @Test
    public void Test1() {
        Report report = new Report("gollem", null, "his wand skill wack");
        assertEquals("gollem", report.getReporter());
        assertNull(report.getReported());
        assertEquals("his wand skill wack", report.getReason());
    }

}
