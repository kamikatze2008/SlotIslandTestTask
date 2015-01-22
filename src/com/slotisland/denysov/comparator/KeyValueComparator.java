package com.slotisland.denysov.comparator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asus on 22.01.2015.
 */
public class KeyValueComparator implements LineComparator {
    private String firstLine;
    private String secondLine;
    private int lineNumber;
    private static Pattern keyPattern = Pattern.compile("[^&]+=");
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
        keyMatcher = keyPattern.matcher(firstLine);
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
        keyMatcher = keyPattern.matcher(secondLine);
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
        StringBuilder resultString=new StringBuilder();
        String tempKey;
        Pattern numberPattern = Pattern.compile("(\\d)+(\\.)*(\\d)*");
        Pattern arrayPattern = Pattern.compile("(\\d(\\.)*(\\d)*,)+\\d(\\.)*(\\d)*");
        Matcher firstNumberMatcher, secondNumberMatcher;
        Matcher firstArrayMatcher,secondArrayMatcher;
        String firstStringValue;
        String secondStringValue;
        while (keySetIterator.hasNext()) {
            tempKey = keySetIterator.next();
            firstStringValue = firstHashMap.get(tempKey);
            secondStringValue = secondHashMap.get(tempKey);
            if (!firstStringValue.equals(secondStringValue)) {
                firstNumberMatcher=numberPattern.matcher(firstStringValue);
                secondNumberMatcher=numberPattern.matcher(secondStringValue);
                firstArrayMatcher=arrayPattern.matcher(firstStringValue);
                secondArrayMatcher=arrayPattern.matcher(secondStringValue);
                if(firstNumberMatcher.matches() && secondNumberMatcher.matches()){
                    double firstValue=Double.parseDouble(firstStringValue);
                    double secondValue=Double.parseDouble(secondStringValue);
                    if(Math.abs(firstValue-secondValue)>0.001){
                        resultString.append("Line ").append(lineNumber).append(" has different numbers in \"").
                                append(tempKey).append("\" tag.\n");
                    }
                } else if(firstArrayMatcher.matches() && secondArrayMatcher.matches()){

                } else{
                    resultString.append("Line ").append(lineNumber).append(" has different value types in \"").
                            append(tempKey).append("\" tag.\n");
                }
            }
        }
        return resultString.toString();
    }
}
