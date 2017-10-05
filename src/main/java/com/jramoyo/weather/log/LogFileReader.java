package com.jramoyo.weather.log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LogFileReader {

    public LogFileReader() {
    }

    public void process(String fileName) {
        try (FileReader fr = new FileReader(fileName); BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                // 1. parse + validate
                // 2. aggregate
                // 2.a. skip past dates
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LogFileReader processor = new LogFileReader();
        processor.process("data.log");
    }
}
