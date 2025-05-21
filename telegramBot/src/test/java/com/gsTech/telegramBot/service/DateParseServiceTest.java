package com.gsTech.telegramBot.service;

import com.gsTech.telegramBot.services.DateParseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateParseServiceTest {

    private DateParseService dateParseService;
    private final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final LocalDateTime fixeDNow = LocalDateTime.of(2025, 5, 25, 10, 0);

    @BeforeEach
    void setUp() {

        Supplier<LocalDateTime> fixedNowSupplier = () -> fixeDNow;
        dateParseService = new DateParseService(fixedNowSupplier);
    }

    @Test
    void shouldParseTodayWithAccentedPrepositionAndTime() {

        String input = "hoje às 14:30";
        LocalDateTime expectedDateTime = fixeDNow.withHour(14).withMinute(30);
        String expected = expectedDateTime.format(outputFormatter);

        String result = dateParseService.parseDate(input);

        assertEquals(expected, result);
    }

    @Test
    void shouldParseTodayWithUnaccentedPrepositionAndTime() {

        String input = "hoje as 09:30";
        LocalDateTime expectedDateTime = fixeDNow.withHour(9).withMinute(30);
        String expected = expectedDateTime.format(outputFormatter);

        String result = dateParseService.parseDate(input);

        assertEquals(expected, result);
    }

    @Test
    void shouldParseTodayExpressionWithoutAs() {

        String input = "hoje 19:15";
        LocalDateTime expectedDateTime = fixeDNow.withHour(19).withMinute(15);
        String expected = expectedDateTime.format(outputFormatter);

        String result = dateParseService.parseDate(input);

        assertEquals(expected, result);
    }

    @Test
    void shouldParseNamedMonthWithAndWithoutYear() {
        String inputWithYear = "25 de maio de 2025 18:00";
        String expectedWithYear = "25/05/2025 18:00";

        String inputWithoutYear = "25 de maio 18:00";
        String expectedWithoutYear = "25/05/2025 18:00";

        assertEquals(expectedWithYear, dateParseService.parseDate(inputWithYear));
        assertEquals(expectedWithoutYear, dateParseService.parseDate(inputWithoutYear));
    }

    @Test
    void shouldReturnErrorMessageWhenMonthIsInvalid() {

        String input = "25 de lkj 20:00";
        String expected = "Mês inválido, digite a data manualmente! (Ex: dd/MM/yyyy HH:mm)";

        String result = dateParseService.parseDate(input);

        assertEquals(expected, result);
    }

    @Test
    void shouldParseWeekdayWithTimeWithAccentOnAs() {

        String input = "segunda às 19:00";
        String expected = "26/05/2025 19:00";

        String result = dateParseService.parseDate(input);

        assertEquals(expected, result);
    }

    @Test
    void shouldParseWeekdayWithTimeWithoutAccentOnAs() {

        String input = "segunda as 19:00";
        String expected = "26/05/2025 19:00";

        String result = dateParseService.parseDate(input);

        assertEquals(expected, result);
    }

    @Test
    void shouldParseFullNumericDateWithTime() {
        String input = "25/12/2025 18:00";
        String expected = "25/12/2025 18:00";

        String result = dateParseService.parseDate(input);

        assertEquals(expected, result);
    }

    @Test
    void shouldParseNumericDateWithTimeWithoutYear() {
        String input = "25/12 18:00";
        int currentYear = LocalDateTime.now().getYear();
        String expected = String.format("25/12/%d 18:00", currentYear);

        String result = dateParseService.parseDate(input);

        assertEquals(expected, result);
    }

    @Test
    void shouldParseNumericDateWithoutTimeWithYear() {
        String input = "25/12/2025";

        LocalDateTime expectedDateTime = LocalDateTime.of(2025, 12, 25, fixeDNow.getHour(), fixeDNow.getMinute());
        String expected = expectedDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        String result = dateParseService.parseDate(input);

        assertEquals(expected, result);
    }

    @Test
    void shouldParseNumericDateWithoutTimeWithoutYear() {
        String input = "25/12";

        int year = fixeDNow.getYear();
        LocalDateTime expectedDateTime = LocalDateTime.of(year, 12, 25, fixeDNow.getHour(), fixeDNow.getMinute(), 0, 0);
        String expected = expectedDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        String result = dateParseService.parseDate(input);

        assertEquals(expected, result);
    }

}
