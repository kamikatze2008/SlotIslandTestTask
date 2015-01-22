package com.slotisland.denysov.comparator;

/**
 * Created by asus on 22.01.2015.
 */
public class OtherComparator implements LineComparator {
    private String firstLine;
    private String secondLine;
    private int lineNumber;

    public OtherComparator(String firstLine, String secondLine, int lineNumber) {
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.lineNumber = lineNumber;
    }

    @Override
    public String compareLines() {
        return new StringBuilder().append("Line ").append(lineNumber).append(" has wrong content\nFile1 content is\n").
                append(firstLine).append("\nFile2 content is\n").
                append(secondLine).append("\n*****\n").toString();
    }
}
