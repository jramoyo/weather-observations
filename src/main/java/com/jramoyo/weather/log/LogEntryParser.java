package com.jramoyo.weather.log;

import com.jramoyo.weather.domain.Observation;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class LogEntryParser {

    private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd'T'hh:mm";

    // <timestamp>|<location>|<temperature>|<observatory>
    public Optional<Observation> parse(String entry) {
        String[] tokens = entry.split("\\|");

        if (!isValid(tokens)) {
            return Optional.empty();
        }

        Date timestamp;
        try {
            timestamp = new SimpleDateFormat(TIMESTAMP_PATTERN).parse(tokens[0]);
        } catch (ParseException ex) {
            return Optional.empty();
        }

        Observation observation = new Observation();

        observation.timestamp = timestamp;

        String[] location = tokens[1].split(",");
        observation.x = new BigDecimal(location[0]);
        observation.y = new BigDecimal(location[1]);

        observation.temperature = new BigDecimal(tokens[2]);
        observation.observatory = tokens[3];

        return Optional.of(observation);
    }

    private boolean isValid(String[] tokens) {
        return validateEntry(tokens)
                && validateLocation(tokens[1])
                && validateTemperature(tokens[2])
                && validateObservatory(tokens[3]);
    }

    private boolean validateObservatory(String token) {
        return !StringUtils.isEmpty(token);
    }

    private boolean validateTemperature(String token) {
        return StringUtils.isNumeric(token);
    }

    private boolean validateEntry(String[] tokens) {
        return tokens.length == 4;
    }

    private boolean validateLocation(String token) {
        String[] location = token.split(",");
        if (location.length != 2) {
            return false;
        } else if (!StringUtils.isNumeric(location[0]) || !StringUtils.isNumeric(location[1])) {
            return false;
        }
        return true;
    }

}