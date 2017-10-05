package com.jramoyo.weather.log;

import com.jramoyo.weather.Observation;
import com.jramoyo.weather.Statistics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class LogFileReader {

    private final Statistics statistics;

    private final LogEntryParser parser;

    public LogFileReader(Statistics statistics) {
        this.statistics = statistics;
        this.parser = new LogEntryParser();
    }

    public void read(String fileName) throws IOException {
        try (FileReader fr = new FileReader(fileName); BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                Optional<Observation> optional = parser.parse(line);
                if (optional.isPresent()) {
                    statistics.read(optional.get());
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage LogFileReader <file_name>");
            System.exit(1);
        }

        String fileName = args[0];

        try {
            System.out.println(String.format("Reading log file: %s", fileName));

            long start = System.currentTimeMillis();
            LogFileReader read = new LogFileReader(new Statistics());
            read.read(fileName);

            System.out.println(String.format("Done, elapsed: %d ms", System.currentTimeMillis() - start));
        } catch (IOException ex) {
            System.err.println("An error occurred while reading log file.");
            ex.printStackTrace();
        }
    }
}
