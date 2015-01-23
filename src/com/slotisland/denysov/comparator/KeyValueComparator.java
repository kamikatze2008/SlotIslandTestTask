package com.slotisland.denysov.comparator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asus on 22.01.2015.
 */
public class KeyValueComparator implements LineComparator {
    private String firstLine;
    private String secondLine;
    private int lineNumber;
    private static final Pattern KEY_PATTERN = Pattern.compile("[^&]+=");
    private HashMap<String, String> firstHashMap;
    private HashMap<String, String> secondHashMap;
    private Set<String> keySet;

    public KeyValueComparator(String firstLine, String secondLine, int lineNumber) {
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.lineNumber = lineNumber;
    }

    @Override
    public String compareLines() {
        StringBuilder resultString = new StringBuilder();
        if (!firstLine.equals(secondLine)) {
            keySet = new LinkedHashSet<String>();
            firstStringSplitter();
            secondStringSplitter();
            resultString.append(compareValues());
        }
        return resultString.toString();
    }

    private void firstStringSplitter() {
        Matcher keyMatcher;
        Pattern valuePattern;
        Matcher valueMatcher;
        String key;
        String value;
        firstHashMap = new HashMap<String, String>();
        keyMatcher = KEY_PATTERN.matcher(firstLine);
        while (keyMatcher.find()) {
            key = keyMatcher.group().replace("=", "");
            keySet.add(key);
            valuePattern = Pattern.compile(key + "=[^&]+");
            valueMatcher = valuePattern.matcher(firstLine);
            while (valueMatcher.find()) {
                value = valueMatcher.group().replace(key + "=", "");
                firstHashMap.put(key, value);
            }
        }
    }

    private void secondStringSplitter() {
        Matcher keyMatcher;
        Pattern valuePattern;
        Matcher valueMatcher;
        String key;
        String value;
        secondHashMap = new HashMap<String, String>();
        keyMatcher = KEY_PATTERN.matcher(secondLine);
        while (keyMatcher.find()) {
            key = keyMatcher.group().replace("=", "");
            keySet.add(key);
            valuePattern = Pattern.compile(key + "=[^&]+");
            valueMatcher = valuePattern.matcher(secondLine);
            while (valueMatcher.find()) {
                value = valueMatcher.group().replace(key + "=", "");
                secondHashMap.put(key, value);
            }
        }
    }

    private String compareValues() {
        Iterator<String> keySetIterator = keySet.iterator();
        StringBuilder resultString = new StringBuilder();
        String tempKey;
        Pattern numberPattern = Pattern.compile("(\\d)+(\\.)*(\\d)*");
        Pattern arrayPattern = Pattern.compile("((\\d)+(\\.)*(\\d)*,)+(\\d)+(\\.)*(\\d)*");
        Matcher firstNumberMatcher, secondNumberMatcher;
        Matcher firstArrayMatcher, secondArrayMatcher;
        String firstStringValue;
        String secondStringValue;
        while (keySetIterator.hasNext()) {
            tempKey = keySetIterator.next();
            firstStringValue = firstHashMap.get(tempKey);
            secondStringValue = secondHashMap.get(tempKey);
            if (!firstStringValue.equals(secondStringValue)) {
                firstNumberMatcher = numberPattern.matcher(firstStringValue);
                secondNumberMatcher = numberPattern.matcher(secondStringValue);
                firstArrayMatcher = arrayPattern.matcher(firstStringValue);
                secondArrayMatcher = arrayPattern.matcher(secondStringValue);
                if (firstNumberMatcher.matches() && secondNumberMatcher.matches()) {
                    numberCheck(resultString, tempKey, firstStringValue, secondStringValue);
                } else if (firstArrayMatcher.matches() && secondArrayMatcher.matches()) {
                    arrayCheck(resultString, tempKey, firstStringValue, secondStringValue);
                } else {
                    resultString.append("Line ").append(lineNumber).append(". Different value types in \"").
                            append(tempKey).append("\" tag.\n");
                }
            }
        }
        return resultString.toString();
    }

    private void arrayCheck(StringBuilder resultString, String tempKey, String firstStringValue, String secondStringValue) {
        StringTokenizer firstStringTokenizer = new StringTokenizer(firstStringValue, ",");
        StringTokenizer secondStringTokenizer = new StringTokenizer(secondStringValue, ",");
        List<Double> firstArrayList = new ArrayList<Double>();
        List<Double> secondArrayList = new ArrayList<Double>();
        while (firstStringTokenizer.hasMoreTokens()) {
            firstArrayList.add(Double.parseDouble(firstStringTokenizer.nextToken()));
        }
        while (secondStringTokenizer.hasMoreTokens()) {
            secondArrayList.add(Double.parseDouble(secondStringTokenizer.nextToken()));
        }
        Collections.sort(firstArrayList);
        Collections.sort(secondArrayList);
        if (!firstArrayList.equals(secondArrayList)) {
            resultString.append("Line ").append(lineNumber).append(". Different arrays in \"").
                    append(tempKey).append("\" tag.\n");
        }
    }

    private void numberCheck(StringBuilder resultString, String tempKey, String firstStringValue, String secondStringValue) {
        double firstValue = Double.parseDouble(firstStringValue);
        double secondValue = Double.parseDouble(secondStringValue);
        if (Math.abs(firstValue - secondValue) > 0.001) {
            resultString.append("Line ").append(lineNumber).append(". Different numbers in \"").
                    append(tempKey).append("\" tag.\n");
        }
    }
}
