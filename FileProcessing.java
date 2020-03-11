package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileProcessing {
    /** making the graph of the map */
    public static void mapMaker() throws FileNotFoundException {
        Main.map = new ArrayList<>(100);
        String[] strings = reader();
        for(int i = 0; i < 100; i++){
            Main.map.add(stringParser(strings[i]));
        }
    }

    /** read the connection file and make a string array */
    public static String[] reader() throws FileNotFoundException {
        String[] lines = new String[100];
        File connection = new File("files/connection.txt");
        Scanner sc = new Scanner(connection);

        for(int i=0; sc.hasNextLine(); i++){
            lines[i] = sc.nextLine();
        }

        return lines;
    }

    /** method to parse a line and turn into an array list */
    public static ArrayList<Integer> stringParser(String input){
        ArrayList<Integer> numberAL = new ArrayList<>();    // container for the numbers in int format
        String number = "";                                 //empty string to begin with
        char[] line = input.toCharArray();                  //convert the string into char array

        for(int i=0; i < line.length; i++){                 //for each character
            if(line[i] == ' '){                             //if its a space
                int x = parseInteger(number);               //then make the number string a int
                numberAL.add(x);                            //add
                number = "";                                //re-empty numberString
            }
            else number += line[i];                         //if its not a space then add it in the string
        }
        numberAL.add(parseInteger(number));                 //for the last number

        return numberAL;
    }

    /** my own parseInt */
    public static int parseInteger(String s){
        int number = 0;
        char[] sArray = s.toCharArray();
        for(int i = 0; i < sArray.length ; i++){
            number = number * 10 + ( (int) sArray[i] - (int) '0');
        }
        return number;
    }

}
