package com.slotisland.denysov.checker;

import com.slotisland.denysov.comparator.KeyValueComparator;
import com.slotisland.denysov.comparator.LineComparator;
import com.slotisland.denysov.comparator.NodeComparator;
import com.slotisland.denysov.comparator.OtherComparator;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asus on 22.01.2015.
 */
public class Checker {
    private BufferedReader firstBufferedReader;
    private BufferedReader secondBufferedReader;
    private StringBuilder differString = new StringBuilder();
    private static final String NODE_PATTERN_STRING = "<(\\w|\\d)+( \\w+=\"(\\w|\\d)+\")*>(\\w|\\d)+</(\\w|\\d)+>";
    private static final String KEY_VALUE_PATTERN_STRING = "(value\\d+=.+&{0,1})+";

    public Checker(String firstFilePath, String secondFilePath) throws FileNotFoundException {
        firstBufferedReader = new BufferedReader(new FileReader(new File(firstFilePath)));
        secondBufferedReader = new BufferedReader(new FileReader(new File(secondFilePath)));
    }

    public String getDifferString() {
        return differString.toString();
    }

    public void check() throws IOException {
        String firstFileString;
        String secondFileString;
        int lineNumber = 1;

        Pattern nodePattern = Pattern.compile(NODE_PATTERN_STRING);
        Matcher firstNodeMatcher, secondNodeMatcher;

        Pattern keyValuePattern = Pattern.compile(KEY_VALUE_PATTERN_STRING);
        Matcher firstKeyValueMatcher, secondKeyValueMatcher;

        LineComparator lineComparator;

        while ((firstFileString = firstBufferedReader.readLine()) != null & (secondFileString = secondBufferedReader.readLine()) != null) {

            firstNodeMatcher = nodePattern.matcher(firstFileString);
            secondNodeMatcher = nodePattern.matcher(secondFileString);

            firstKeyValueMatcher = keyValuePattern.matcher(firstFileString);
            secondKeyValueMatcher = keyValuePattern.matcher(secondFileString);

            if (firstNodeMatcher.matches() && secondNodeMatcher.matches()) {
                lineComparator = new NodeComparator(firstFileString, secondFileString, lineNumber);
                differString.append(lineComparator.compareLines());
            } else if (firstKeyValueMatcher.matches() && secondKeyValueMatcher.matches()) {
                lineComparator = new KeyValueComparator(firstFileString, secondFileString, lineNumber);
                differString.append(lineComparator.compareLines());
            } else {
                lineComparator = new OtherComparator(firstFileString, secondFileString, lineNumber);
                differString.append(lineComparator.compareLines());
            }

            lineNumber++;

        }
        finalizedCheck(firstFileString, secondFileString, lineNumber);
        equivalentCheck();
    }

    private void equivalentCheck() {
        if (differString.toString().equals("")) {
            differString.append("Files are equivalent");
        }
    }

    private void finalizedCheck(String firstFileString, String secondFileString, int lineNumber) throws IOException {
        if (firstFileString != null) {
            differString.append("Strings appeared in the first file and didn't appear in the second file:\n");
        }
        while (firstFileString != null) {
            differString.append("Line ").append(lineNumber).append(".\t").append(firstFileString).append("\n");
            lineNumber++;
            firstFileString = firstBufferedReader.readLine();
            differString.append(firstFileString);
        }
        if (secondFileString != null) {
            differString.append("Strings appeared in the second file and didn't appear in the first file:\n");
        }
        while (secondFileString != null) {
            differString.append("Line ").append(lineNumber).append(".\t").append(secondFileString).append("\n");
            lineNumber++;
            secondFileString = secondBufferedReader.readLine();
        }
    }
}
