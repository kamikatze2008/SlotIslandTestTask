package com.slotisland.denysov.comparator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asus on 22.01.2015.
 */
public class NodeComparator implements LineComparator {
    private String firstLine;
    private String secondLine;
    private int lineNumber;
    private static final Pattern START_NODE_PATTERN = Pattern.compile("<[^\\s]+");
    private static final Pattern END_NODE_PATTERN = Pattern.compile("</[^>]+");
    private static final Pattern NODE_CONTAINMENT_PATTERN = Pattern.compile(">[^<]+");
    private static final Pattern KEY_PATTERN = Pattern.compile(" [^=]+");
    private static final Pattern VALUE_PATTERN = Pattern.compile("\"[^\"]+");
    private HashMap<String, String> firstHashMap;
    private HashMap<String, String> secondHashMap;
    private Set<String> keySet;

    public NodeComparator(String firstLine, String secondLine, int lineNumber) {
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.lineNumber = lineNumber;
    }

    @Override
    public String compareLines() {
        StringBuilder resultString = new StringBuilder();
        tagNameCheck(resultString);
        containmentCheck(resultString);


        return resultString.toString();
    }

    private void containmentCheck(StringBuilder resultString) {
        Matcher firstContainmentMatcher = NODE_CONTAINMENT_PATTERN.matcher(firstLine);
        Matcher secondContainmentMatcher = NODE_CONTAINMENT_PATTERN.matcher(secondLine);
        if (firstContainmentMatcher.find() && secondContainmentMatcher.find()) {
            if (!firstContainmentMatcher.group().equals(secondContainmentMatcher.group())) {
                resultString.append("Line " + lineNumber + ". Different tags containment.\n");
            }
        }
    }

    private void tagNameCheck(StringBuilder resultString) {
        Matcher firstStartNodeMatcher = START_NODE_PATTERN.matcher(firstLine);
        Matcher secondStartNodeMatcher = START_NODE_PATTERN.matcher(secondLine);
        Matcher firstEndNodeMatcher = END_NODE_PATTERN.matcher(firstLine);
        Matcher secondEndNodeMatcher = END_NODE_PATTERN.matcher(secondLine);
        String firstStartNode = null, firstEndNode = null, secondStartNode = null, secondEndNode = null;
        if (firstStartNodeMatcher.find() && secondStartNodeMatcher.find() && firstEndNodeMatcher.find() && secondEndNodeMatcher.find()) {
            firstStartNode = firstStartNodeMatcher.group().replace("<", "");
            secondStartNode = secondStartNodeMatcher.group().replace("<", "");
            firstEndNode = firstEndNodeMatcher.group().replace("</", "");
            secondEndNode = secondEndNodeMatcher.group().replace("</", "");
            if (!firstStartNode.equals(firstEndNode) || !secondStartNode.equals(secondEndNode)) {
                resultString.append("Line " + lineNumber + ". Node names error.\n");
            } else if (!firstStartNode.equals(secondStartNode)) {
                resultString.append("Line " + lineNumber + ". Tag Name differs in files.\n");
            }
        }

    }
}
