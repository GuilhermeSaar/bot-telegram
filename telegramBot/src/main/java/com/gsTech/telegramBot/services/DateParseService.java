package com.gsTech.telegramBot.services;

import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DateParseService {


    public String parseDate(String input) {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


        // "hoje as HH:mm"
        Pattern todayPattern = Pattern.compile("hoje a?s? (\\d{2}:\\d{2})", Pattern.CASE_INSENSITIVE);
        Matcher todayMatcher = todayPattern.matcher(input);

        if(todayMatcher.find()) {

            String time = todayMatcher.group(1);
            LocalTime parsedTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime dateTime = now.withHour(parsedTime.getHour()).withMinute(parsedTime.getMinute());

            return dateTime.format(outputFormatter);
        }

        // "com ou sem o ano"
        Pattern datePattern = Pattern.compile("(\\d{1,2}) de (\\w+)(?: de (\\d{4}))? (\\{2}:\\d{2})", Pattern.CASE_INSENSITIVE);
        Matcher dateMatcher = datePattern.matcher(input);

        if(dateMatcher.find()) {

            int day = Integer.parseInt(dateMatcher.group(1));
            String monthName = dateMatcher.group(2).toLowerCase();
            String yearStr = dateMatcher.group(3);
            String time = dateMatcher.group(4);

            // mapeando os nomes para numeros do mes
            Map<String, Integer> months = Map.ofEntries(
                    Map.entry("janeiro",1),
                    Map.entry("fevereiro", 2),
                    Map.entry("março", 3),
                    Map.entry("abril", 4),
                    Map.entry("maio", 5),
                    Map.entry("junho", 6),
                    Map.entry("julho", 7),
                    Map.entry("agosto", 8),
                    Map.entry("setembro", 9),
                    Map.entry("outubro", 10),
                    Map.entry("novembro", 11),
                    Map.entry("dezembro", 12)
            );

            int month = months.getOrDefault(monthName, now.getMonthValue());
            int year = (yearStr != null) ? Integer.parseInt(yearStr) : now.getYear();

            LocalTime parsedTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime dateTime = LocalDateTime.of(year, month, day, parsedTime.getHour(), parsedTime.getMinute());

            return dateTime.format(outputFormatter);
        }


        // dia da semana com hora
        Pattern weekDayPattern = Pattern.compile(
                "(segunda|terça|terca|quarta|quinta|sexta|sábado|sabado|domingo)[ ]*a?s?[ ]*(\\d{2}:\\d{2})",Pattern.CASE_INSENSITIVE);

        Matcher weekDayMatcher = weekDayPattern.matcher(input);

        if(weekDayMatcher.find()) {

            String dayName = weekDayMatcher.group(1).toLowerCase();
            String time = weekDayMatcher.group(2);

            Map<String, DayOfWeek> daysOfWeek = Map.ofEntries(
                    Map.entry("segunda", DayOfWeek.MONDAY),
                    Map.entry("terca", DayOfWeek.TUESDAY),
                    Map.entry("terça", DayOfWeek.TUESDAY),
                    Map.entry("quarta", DayOfWeek.WEDNESDAY),
                    Map.entry("quinta", DayOfWeek.THURSDAY),
                    Map.entry("sexta", DayOfWeek.FRIDAY),
                    Map.entry("sabado", DayOfWeek.SATURDAY),
                    Map.entry("domingo", DayOfWeek.SUNDAY)
            );

            DayOfWeek targetDay = daysOfWeek.get(dayName);
            LocalTime parseTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));

            LocalDateTime dateTime = now.with(TemporalAdjusters.nextOrSame(targetDay))
                    .withHour(parseTime.getHour())
                    .withMinute(parseTime.getMinute());

            return dateTime.format(outputFormatter);
        }

        // data numerica com ou sem ano e hora
        Pattern numericDatePattern = Pattern.compile(
                "(\\d{1,2})/(\\d{1,2})(?:/(\\d{4}))?(?:[ ]*(\\d{2}:\\d{2}))?",
                Pattern.CASE_INSENSITIVE
        );
        Matcher numericDateMatcher = numericDatePattern.matcher(input);

        if (numericDateMatcher.find()) {
            int day = Integer.parseInt(numericDateMatcher.group(1));
            int month = Integer.parseInt(numericDateMatcher.group(2));
            String yearStr = numericDateMatcher.group(3);
            String time = numericDateMatcher.group(4);

            int year = (yearStr != null) ? Integer.parseInt(yearStr) : now.getYear();
            LocalTime parsedTime = (time != null)
                    ? LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
                    : LocalTime.of(now.getHour(), now.getMinute());

            LocalDateTime dateTime = LocalDateTime.of(year, month, day, parsedTime.getHour(), parsedTime.getMinute());

            return dateTime.format(outputFormatter);
        }

        return "Data não reconhecida, digite manualmente no formato: dd/MM/yyyy HH:mm";
    }

}
