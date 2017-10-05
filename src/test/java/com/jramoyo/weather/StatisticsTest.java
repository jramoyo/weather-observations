package com.jramoyo.weather;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(BlockJUnit4ClassRunner.class)
public class StatisticsTest {

    private Statistics statistics;

    @Before
    public void setup() {
        statistics = new Statistics();
    }

    @Test
    public void it_calculates_the_number_of_observations_from_each_observatory() {
        Observation observation1 = new Observation();
        observation1.observatory = "US";

        Observation observation2 = new Observation();
        observation2.observatory = "AU";

        Observation observation3 = new Observation();
        observation3.observatory = "US";

        Observation observation4 = new Observation();
        observation4.observatory = "AU";

        Observation observation5 = new Observation();
        observation5.observatory = "FR";

        statistics.read(observation1);
        statistics.read(observation2);
        statistics.read(observation3);
        statistics.read(observation4);
        statistics.read(observation5);

        assertThat(statistics.getDistribution().get("AU"), is(2));
        assertThat(statistics.getDistribution().get("US"), is(2));
        assertThat(statistics.getDistribution().get("FR"), is(1));
        assertThat(statistics.getDistribution().get("PH"), nullValue(Integer.class));
    }

    @Test
    public void it_calculates_the_statistics_on_temperature() {
        Observation observation1 = new Observation();
        observation1.observatory = "FR";
        observation1.temperature = BigDecimal.valueOf(10);

        Observation observation2 = new Observation();
        observation2.observatory = "FR";
        observation2.temperature = BigDecimal.valueOf(5);

        Observation observation3 = new Observation();
        observation3.observatory = "FR";
        observation3.temperature = BigDecimal.valueOf(1);

        Observation observation4 = new Observation();
        observation4.observatory = "FR";
        observation4.temperature = BigDecimal.valueOf(-20);

        Observation observation5 = new Observation();
        observation5.observatory = "FR";
        observation5.temperature = BigDecimal.valueOf(-10);

        statistics.read(observation1);
        statistics.read(observation2);
        statistics.read(observation3);
        statistics.read(observation4);
        statistics.read(observation5);

        assertThat(statistics.getMinTemperature(), is(new BigDecimal("-20.00")));
        assertThat(statistics.getMaxTemperature(), is(new BigDecimal("10.00")));
        assertThat(statistics.getMeanTemperature(), is(new BigDecimal("-2.80")));
    }

    @Test
    public void it_converts_read_temperatures_to_kelvin() {
        Observation observation1 = new Observation();
        observation1.observatory = "AU";
        observation1.temperature = BigDecimal.valueOf(-173.15); // 100K

        Observation observation2 = new Observation();
        observation2.observatory = "US";
        observation2.temperature = BigDecimal.valueOf(-279.67); // 100K

        Observation observation3 = new Observation();
        observation3.observatory = "FR";
        observation3.temperature = BigDecimal.valueOf(100);     // 100K

        Observation observation4 = new Observation();
        observation4.observatory = "PH";
        observation4.temperature = BigDecimal.valueOf(100);     // 100K

        statistics.read(observation1);
        statistics.read(observation2);
        statistics.read(observation3);
        statistics.read(observation4);

        assertThat(statistics.getMinTemperature(), is(new BigDecimal("100.00")));
        assertThat(statistics.getMaxTemperature(), is(new BigDecimal("100.00")));
        assertThat(statistics.getMeanTemperature(), is(new BigDecimal("100.00")));
    }

    @Test
    public void it_converts_output_temperatures_to_the_specified_locale() {
        Observation observation = new Observation();
        observation.temperature = BigDecimal.valueOf(100); // 100K
        statistics.read(observation);

        assertThat(statistics.getMinTemperature(), is(new BigDecimal("100.00")));
        assertThat(statistics.getMaxTemperature(), is(new BigDecimal("100.00")));
        assertThat(statistics.getMeanTemperature(), is(new BigDecimal("100.00")));

        assertThat(statistics.getMinTemperature("AU"), is(new BigDecimal("-173.15")));
        assertThat(statistics.getMaxTemperature("AU"), is(new BigDecimal("-173.15")));
        assertThat(statistics.getMeanTemperature("AU"), is(new BigDecimal("-173.15")));

        assertThat(statistics.getMinTemperature("US"), is(new BigDecimal("-279.67")));
        assertThat(statistics.getMaxTemperature("US"), is(new BigDecimal("-279.67")));
        assertThat(statistics.getMeanTemperature("US"), is(new BigDecimal("-279.67")));

        assertThat(statistics.getMinTemperature("FR"), is(new BigDecimal("100.00")));
        assertThat(statistics.getMaxTemperature("FR"), is(new BigDecimal("100.00")));
        assertThat(statistics.getMeanTemperature("FR"), is(new BigDecimal("100.00")));

        assertThat(statistics.getMinTemperature("PH"), is(new BigDecimal("100.00")));
        assertThat(statistics.getMaxTemperature("PH"), is(new BigDecimal("100.00")));
        assertThat(statistics.getMeanTemperature("PH"), is(new BigDecimal("100.00")));
    }

    @Test
    public void it_calculates_the_total_distance_travelled() {
        Observation observation1 = new Observation();
        observation1.observatory = "AU";
        observation1.x = BigDecimal.ZERO;
        observation1.y = BigDecimal.ZERO;

        Observation observation2 = new Observation();
        observation2.observatory = "AU";
        observation2.x = BigDecimal.ONE;
        observation2.y = BigDecimal.ONE;

        Observation observation3 = new Observation();
        observation3.observatory = "AU";
        observation3.x = BigDecimal.valueOf(2);
        observation3.y = BigDecimal.valueOf(2);

        Observation observation4 = new Observation();
        observation4.observatory = "AU";
        observation4.x = BigDecimal.valueOf(3);
        observation4.y = BigDecimal.valueOf(3);

        Observation observation5 = new Observation();
        observation5.observatory = "AU";
        observation5.x = BigDecimal.valueOf(4);
        observation5.y = BigDecimal.valueOf(4);

        statistics.read(observation1);
        statistics.read(observation2);
        statistics.read(observation3);
        statistics.read(observation4);
        statistics.read(observation5);

        assertThat(statistics.getTotalDistanceTravelled(), is(new BigDecimal("5.64")));
    }

    @Test
    public void it_converts_read_distance_to_km() {
        Observation observation1 = new Observation();
        observation1.observatory = "AU";
        observation1.x = BigDecimal.valueOf(100);     // 100Km
        observation1.y = BigDecimal.valueOf(100);     // 100Km

        Observation observation2 = new Observation();
        observation2.observatory = "US";
        observation2.x = BigDecimal.valueOf(62.1371); // 100Km
        observation2.y = BigDecimal.valueOf(62.1371); // 100Km

        Observation observation3 = new Observation();
        observation3.observatory = "FR";
        observation3.x = BigDecimal.valueOf(100000);  // 100Km
        observation3.y = BigDecimal.valueOf(100000);  // 100Km

        statistics.read(observation1);
        statistics.read(observation2);
        statistics.read(observation3);

        assertThat(statistics.getTotalDistanceTravelled(), is(new BigDecimal("0.00"))); // zero means location did not change
    }

    @Test
    public void it_converts_output_distance_to_the_specified_locale() {
        Observation observation1 = new Observation();
        observation1.observatory = "AU";
        observation1.x = BigDecimal.valueOf(0); // 100Km
        observation1.y = BigDecimal.valueOf(0); // 100Km

        Observation observation2 = new Observation();
        observation2.observatory = "AU";
        observation2.x = BigDecimal.valueOf(100); // 100Km
        observation2.y = BigDecimal.valueOf(100); // 100Km

        statistics.read(observation1);
        statistics.read(observation2);

        assertThat(statistics.getTotalDistanceTravelled(), is(new BigDecimal("141.42")));
        assertThat(statistics.getTotalDistanceTravelled("AU"), is(new BigDecimal("141.42")));
        assertThat(statistics.getTotalDistanceTravelled("US"), is(new BigDecimal("87.87")));
        assertThat(statistics.getTotalDistanceTravelled("FR"), is(new BigDecimal("141420.00")));
        assertThat(statistics.getTotalDistanceTravelled("PH"), is(new BigDecimal("141.42")));
    }

    @Test
    public void it_ignores_observations_with_unordered_timestamp() {
        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 1000);

        Observation observation1 = new Observation();
        observation1.timestamp = date1;

        Observation observation2 = new Observation();
        observation2.timestamp = date2;

        statistics.read(observation2);
        statistics.read(observation1); // wrong order

        assertThat(statistics.getCount(), is(1L));
        assertThat(statistics.getSkipped(), is(1L));
    }

}
