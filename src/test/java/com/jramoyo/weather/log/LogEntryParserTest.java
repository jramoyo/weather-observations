package com.jramoyo.weather.log;

import com.jramoyo.weather.domain.Observation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(BlockJUnit4ClassRunner.class)
public class LogEntryParserTest {

    private LogEntryParser parser = new LogEntryParser();

    @Test
    public void it_returns_an_observation_if_the_entry_is_valid() {
        Optional<Observation> optional = parser.parse("2014-12-31T13:44|10,5|243|AU");
        assertThat(optional.isPresent(), is(true));

        Observation observation = optional.get();
        assertThat(observation.timestamp, notNullValue());
        assertThat(observation.x, is(new BigDecimal("10")));
        assertThat(observation.y, is(new BigDecimal("5")));
        assertThat(observation.temperature, is(new BigDecimal("243")));
        assertThat(observation.observatory, is("AU"));
    }

    @Test
    public void it_returns_an_empty_value_if_the_entry_has_incorrect_data() {
        assertThat(parser.parse("-12-31T13:44|10,5|243|AU").isPresent(), is(false));
        assertThat(parser.parse("2014-12-31T13:44|1|243|AU").isPresent(), is(false));
        assertThat(parser.parse("2014-12-31T13:44|x,y|243|AU").isPresent(), is(false));
        assertThat(parser.parse("2014-12-31T13:44|10,5|abc|AU").isPresent(), is(false));
    }

    @Test
    public void it_returns_an_empty_value_if_the_entry_has_empty_data() {
        assertThat(parser.parse("|10,5|243|AU").isPresent(), is(false));
        assertThat(parser.parse("2014-12-31T13:44||243|AU").isPresent(), is(false));
        assertThat(parser.parse("2014-12-31T13:44|10,5||AU").isPresent(), is(false));
        assertThat(parser.parse("2014-12-31T13:44|10,5|243|").isPresent(), is(false));
    }

    @Test
    public void it_returns_an_empty_value_if_the_entry_has_missing_data() {
        assertThat(parser.parse("10,5|243|AU").isPresent(), is(false));
        assertThat(parser.parse("2014-12-31T13:44|243|AU").isPresent(), is(false));
        assertThat(parser.parse("2014-12-31T13:44|10,5|AU").isPresent(), is(false));
        assertThat(parser.parse("2014-12-31T13:44|10,5|243").isPresent(), is(false));
    }
}
