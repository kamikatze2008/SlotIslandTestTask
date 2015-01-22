package com.slotisland.denysov.runner;

import com.slotisland.denysov.checker.Checker;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by asus on 22.01.2015.
 */
public class Runner {
    public static final String FIRST_FILE_PATH = "firstFile";
    public static final String SECOND_FILE_PATH = "secondFile";

    public static void main(String[] args) {
        try {
            Checker checker = new Checker(FIRST_FILE_PATH, SECOND_FILE_PATH);
            checker.check();
            System.out.println(checker.getDifferString());
        } catch (FileNotFoundException e){
            System.out.println("Something Wrong With FilePaths!");
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("Can't read files!");
            e.printStackTrace();
        }
    }
}
