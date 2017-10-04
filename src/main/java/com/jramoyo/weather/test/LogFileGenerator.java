package com.jramoyo.weather.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class LogFileGenerator {

    private final String fileName;
    private final long noOfEntries;

    public LogFileGenerator(String fileName, long noOfEntries) {
        this.fileName = fileName;
        this.noOfEntries = noOfEntries;
    }

    public void generate(long startPosition) throws IOException {
        LogEntryGenerator generator = new LogEntryGenerator(new Date());
        File file = new File(fileName);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (long i = startPosition; i < startPosition + noOfEntries; i++) {
                bw.append(generator.generate(i)).append('\n');
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage LogFileGenerator <file_name> <no_of_entries>");
            System.exit(1);
        }

        String fileName = args[0];
        long noOfEntries = Long.parseLong(args[1]);

        try {
            System.out.println(String.format("Generating log file: %s, %d entries", fileName, noOfEntries));

            long start = System.currentTimeMillis();
            LogFileGenerator generator = new LogFileGenerator(fileName, noOfEntries);
            generator.generate(0);

            System.out.println(String.format("Done, elapsed: %d ms", System.currentTimeMillis() - start));
        } catch (IOException ex) {
            System.err.println("An error occurred while generating log file.");
            ex.printStackTrace();
        }
    }
}