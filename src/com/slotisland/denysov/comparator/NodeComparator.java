package com.slotisland.denysov.comparator;

/**
 * Created by asus on 22.01.2015.
 */
public class NodeComparator implements LineComparator{
    private String firstLine;
    private String secondLine;
    private int lineNumber;

    public NodeComparator(String firstLine, String secondLine, int lineNumber) {
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.lineNumber = lineNumber;
    }

    @Override
    public String compareLines() {
        return null;
    }
}
