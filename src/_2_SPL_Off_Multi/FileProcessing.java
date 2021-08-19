package _2_SPL_Off_Multi;

import static _2_SPL_Off_Multi.MPGame.map1;
import static _2_SPL_Off_Multi.MPGame.map2;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Fahim Morshed
 * IITDU, BSSE1102
 * Email: bsse1102@iit.du.ac.bd
 * 
 */

public class FileProcessing {
        /* making the graph of the map */
    public FileProcessing(int rc) throws FileNotFoundException{
        map1 = new ArrayList<>(100);
        String[] strings = reader(1,rc);
        for(int i = 0; i < 100; i++){
            map1.add(stringParser(strings[i]));
        }
        
        map2 = new ArrayList<>(100);
        strings = reader(2,rc);
        for(int i = 0; i < 100; i++){
            map2.add(stringParser(strings[i]));
        }
    }

    /* read the connection file and make a string array */
    public static String[] reader(int num,int rc) throws FileNotFoundException {
        String[] lines = new String[100];
        File connection;
        if (num == 1) 
            connection = new File("src\\_4_Files\\"+rc+"\\connection1.txt");
        else
            connection = new File("src\\_4_Files\\"+rc+"\\connection2.txt");
        Scanner sc = new Scanner(connection);

        for(int i=0; sc.hasNextLine(); i++){
            lines[i] = sc.nextLine();
        }

        return lines;
    }

    /* method to parse a line and turn into an array list */
    public static ArrayList<Integer> stringParser(String input){
        ArrayList<Integer> numberAL = new ArrayList<>();    // container for the numbers in int format
        StringBuilder number = new StringBuilder();                                 //empty string to begin with
        char[] line = input.toCharArray();                  //convert the string into char array

        for (char c : line) {                 //for each character
            if (c == ' ') {                             //if its a space
                int x = parseInteger(number.toString());               //then make the number string a int
                numberAL.add(x);                            //add
                number = new StringBuilder();                                //re-empty numberString
            }
            else number.append(c);                         //if its not a space then add it in the string
        }
        numberAL.add(parseInteger(number.toString()));                 //for the last number

        return numberAL;
    }

    /* my own parseInt */
    public static int parseInteger(String s){
        int number = 0;
        char[] sArray = s.toCharArray();
        for (char c : sArray) {
            number = number * 10 + ((int) c - (int) '0');
        }
        return number;
    }
}
