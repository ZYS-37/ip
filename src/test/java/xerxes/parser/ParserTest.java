package xerxes.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class ParserTest {

    @Test
    void formatDate_validDate_returnsParsedDate() {
        assertEquals(
                LocalDate.of(2026, 8, 24),
                Parser.formatDate("24/8/2026")
        );
    }

    @Test
    void formatDate_singleDigitDayAndMonth_returnsParsedDate() {
        assertEquals(
                LocalDate.of(2026, 3, 2),
                Parser.formatDate("2/3/2026")
        );
    }

    @Test
    void formatDate_wrongFormat_throwsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Parser.formatDate("2026-03-02")
        );
    }

    @Test
    void formatDate_invalidCalendarDate_throwsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Parser.formatDate("31/2/2026")
        );
    }

    @Test
    void formatDate_emptyInput_throwsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Parser.formatDate("")
        );
    }
}
