package com.jramoyo.weather.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Observation {
    public Date timestamp;
    public String observatory;
    public BigDecimal x;
    public BigDecimal y;
    public BigDecimal temperature;
}
