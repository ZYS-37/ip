package xerxes.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Tests date parsing performed by {@link Parser}.
 */

class ParserTest {
    /** Verifies that a correctly formatted date is converted to the expected value. */
    @Test
    void formatDate_validDate_returnsParsedDate() {
        assertEquals(
                LocalDate.of(2026, 8, 24),
                Parser.formatDate("24/8/2026")
        );
    }

    /** Verifies that single-digit days and months are accepted. */
    @Test
    void formatDate_singleDigitDayAndMonth_returnsParsedDate() {
        assertEquals(
                LocalDate.of(2026, 3, 2),
                Parser.formatDate("2/3/2026")
        );
    }

    /** Verifies that dates outside the accepted input format are rejected. */
    @Test
    void formatDate_wrongFormat_throwsException() {
        assertThrows(
                IllegalArgumentException.class, () -> Parser.formatDate("2026-03-02")
        );
    }

    /** Verifies that impossible calendar dates are rejected. */
    @Test
    void formatDate_invalidCalendarDate_throwsException() {
        assertThrows(
                IllegalArgumentException.class, () -> Parser.formatDate("31/2/2026")
        );
    }

    /** Verifies that an empty date input is rejected. */
    @Test
    void formatDate_emptyInput_throwsException() {
        assertThrows(
                IllegalArgumentException.class, () -> Parser.formatDate("")
        );
    }
}
