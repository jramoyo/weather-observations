package com.jramoyo.weather;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class Statistics {

    private long count = 0;
    private long skipped = 0;
    private Date lastTimestamp;

    private Map<String, Integer> distribution = new HashMap<>();

    private BigDecimal minTemperature = round(BigDecimal.valueOf(Integer.MAX_VALUE));
    private BigDecimal maxTemperature = round(BigDecimal.valueOf(Integer.MIN_VALUE));
    private BigDecimal sumTemperature = round(BigDecimal.ZERO);

    private BigDecimal lastX;
    private BigDecimal lastY;
    private BigDecimal totalDistanceTravelled = round(BigDecimal.ZERO);

    public void read(Observation observation) {
        if (lastTimestamp != null && observation.timestamp.before(lastTimestamp)) {
            skipped++;
            return;
        }

        count++;
        lastTimestamp = observation.timestamp;

        readDistribution(observation);
        readTemperature(observation);
        readLocation(observation);
    }

    private void readDistribution(Observation observation) {
        Integer total = distribution.get(observation.observatory);
        if (total == null) {
            distribution.put(observation.observatory, 1);
        } else {
            distribution.put(observation.observatory, ++total);
        }
    }

    private void readTemperature(Observation observation) {
        if (observation.temperature == null) {
            return;
        }

        BigDecimal temperature = convertTemperature(observation.observatory, observation.temperature);
        if (temperature.compareTo(minTemperature) < 0) {
            minTemperature = temperature;
        }

        if (temperature.compareTo(maxTemperature) > 0) {
            maxTemperature = temperature;
        }

        sumTemperature = sumTemperature.add(temperature);
    }

    private BigDecimal convertTemperature(String observatory, BigDecimal temperature) {
        switch (observatory) {
            case "AU": // c -> k
                return temperature.add(BigDecimal.valueOf(273.15));
            case "US": // f -> k
                return temperature
                        .subtract(BigDecimal.valueOf(32))
                        .multiply(BigDecimal.valueOf(5.00 / 9.00))
                        .add(BigDecimal.valueOf(273.15));
            default:
                return temperature;
        }
    }

    private void readLocation(Observation observation) {
        if (observation.x == null || observation.y == null) {
            return;
        }

        BigDecimal x = convertLocation(observation.observatory, observation.x);
        BigDecimal y = convertLocation(observation.observatory, observation.y);

        if (lastX != null && lastY != null) {
            double x1 = lastX.doubleValue();
            double x2 = x.doubleValue();

            double y1 = lastY.doubleValue();
            double y2 = y.doubleValue();

            double distance = Math.hypot(x1 - x2, y1 - y2);
            BigDecimal distanceTravelled = round(BigDecimal.valueOf(distance));
            totalDistanceTravelled = totalDistanceTravelled.add(distanceTravelled);
        }
        lastX = x;
        lastY = y;
    }

    private BigDecimal convertLocation(String observatory, BigDecimal location) {
        switch (observatory) {
            case "US": // mi -> km
                return location.multiply(BigDecimal.valueOf(1.60934));
            case "FR": // m  -> km
                return !location.equals(BigDecimal.ZERO) ? location.divide(BigDecimal.valueOf(1000), BigDecimal.ROUND_HALF_UP) : location;
            default:
                return location;
        }
    }

    private BigDecimal round(BigDecimal bigDecimal) {
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public long getCount() {
        return count;
    }

    public long getSkipped() {
        return skipped;
    }

    public Map<String, Integer> getDistribution() {
        return Collections.unmodifiableMap(distribution);
    }

    public BigDecimal getMinTemperature() {
        return round(minTemperature);
    }

    public BigDecimal getMaxTemperature() {
        return round(maxTemperature);
    }

    public BigDecimal getMeanTemperature() {
        return !sumTemperature.equals(BigDecimal.ZERO) ? round(sumTemperature.divide(BigDecimal.valueOf(count), BigDecimal.ROUND_HALF_UP)) : sumTemperature;
    }

    public BigDecimal getTotalDistanceTravelled() {
        return totalDistanceTravelled;
    }
}
