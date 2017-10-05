package com.jramoyo.weather.log;

import com.jramoyo.weather.Observation;
import com.jramoyo.weather.Statistics;

import java.io.*;
import java.util.Map;
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

    public String createReport(String observatory) {
        StringBuilder sb = new StringBuilder();
        sb.append("Minimum Temperature      : ").append(statistics.getMinTemperature(observatory)).append('\n');
        sb.append("Maximum Temperature      : ").append(statistics.getMaxTemperature(observatory)).append('\n');
        sb.append("Mean Temperature         : ").append(statistics.getMeanTemperature(observatory)).append('\n');
        sb.append("Total Distance Travelled : ").append(statistics.getTotalDistanceTravelled(observatory)).append('\n');
        sb.append('\n');
        sb.append("Observations from each Observatory:").append("\n\n");
        for (Map.Entry<String, Integer> entry : statistics.getDistribution().entrySet()) {
            sb.append("  ").append(entry.getKey()).append(" : ").append(entry.getValue()).append('\n');
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage LogFileReader <input_file_name> <report_file_name> [<locale>]");
            System.exit(1);
        }

        String infileName = args[0];
        String outFileName = args[1];
        String observatory = args.length == 3 ? args[2] : null;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outFileName))) {
            System.out.println(String.format("Reading log file: %s, locale: %s", infileName, observatory));

            long start = System.currentTimeMillis();
            LogFileReader reader = new LogFileReader(new Statistics());
            reader.read(infileName);

            bw.append(reader.createReport(observatory));
            System.out.println(String.format("Done, elapsed: %d ms", System.currentTimeMillis() - start));
        } catch (IOException ex) {
            System.err.println("An error occurred while reading log file.");
            ex.printStackTrace();
        }
    }
}
