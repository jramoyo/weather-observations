package com.jramoyo.weather.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public final class LogEntryGenerator {

    // controls the occurrence of erroneous entries in the log
    private static final int MISSING_FIELD_PERCENTAGE = 10;
    private static final int INVALID_FIELD_PERCENTAGE = 10;
    private static final int UNORDERED_DATE_PERCENTAGE = 10;

    private final Date startDate;

    public LogEntryGenerator(Date startDate) {
        this.startDate = new Date(startDate.getTime());
    }

    public String generate(long position) {
        String timestamp = generateTimestamp(position);
        String observatory = generateObservatory();
        String location = generateLocation(observatory);
        String temperature = generateTemperature(observatory);
        return timestamp + '|' + location + '|' + temperature + '|' + observatory;
    }

    private String generateTimestamp(long position) {
        long modifier = position;
        String pattern = "yyyy-MM-dd'T'hh:mm";

        if (shouldError(MISSING_FIELD_PERCENTAGE)) {
            return "";
        }

        if (shouldError(INVALID_FIELD_PERCENTAGE)) {
            pattern = "yy-MM-dd'T'hh:mm";
        }

        if (shouldError(UNORDERED_DATE_PERCENTAGE)) {
            modifier = position - randomInt(1, 60); // step back to an hour
        }

        long minutes = 60_000;
        long time = startDate.getTime();

        Date newDate = new Date(time + (modifier * minutes)); // every log entry position is 1 minute apart
        return new SimpleDateFormat(pattern).format(newDate);
    }

    private String generateLocation(String observatory) {
        char delimiter = ',';

        if (shouldError(MISSING_FIELD_PERCENTAGE)) {
            return "";
        }

        if (shouldError(INVALID_FIELD_PERCENTAGE)) {
            delimiter = ';';
        }

        int x;
        int y;

        switch (observatory) {
            case "US":
                x = randomInt(0, 621);     // mi
                y = randomInt(0, 621);     // mi
                break;
            case "FR":
                x = randomInt(0, 1000000); // m
                y = randomInt(0, 1000000); // m
                break;
            case "AU":
            default:
                x = randomInt(0, 1000);    // km
                y = randomInt(0, 1000);    // km
        }

        return "" + x + delimiter + y;
    }

    private String generateTemperature(String observatory) {
        if (shouldError(MISSING_FIELD_PERCENTAGE)) {
            return "";
        }

        if (shouldError(INVALID_FIELD_PERCENTAGE)) {
            return "abc"; // return non-numeric value
        }

        int temperature;

        switch (observatory) {
            case "AU":
                temperature = randomInt(-100, 100); // celsius
                break;
            case "US":
                temperature = randomInt(-148, 212); // fahrenheit
                break;
            case "FR":
            default:
                temperature = randomInt(173, 373);  // kelvin
        }

        return "" + temperature;
    }

    private String generateObservatory() {
        if (shouldError(MISSING_FIELD_PERCENTAGE)) {
            return "";
        }

        String[] observatories = Locale.getISOCountries();
        int random = randomInt(0, observatories.length);
        return observatories[random];
    }


    private boolean shouldError(int percentage) {
        int threshold = 100 - percentage;
        return randomInt(0, 100) > threshold;
    }

    private int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}
