/*
 * Venkata Harish Kajur 8982
 * Jonathan Lysiak 4477
 * CS-345
 * Homework 6
 */

import java.io.*;
import java.util.*;

/*
 * Stems files to associate them to their root word in the lexicon
 */
public class stemming {

    public static String stem(String word){

		if(word.endsWith("ies")){
			if(!word.endsWith("eies") && !word.endsWith("aies")){
				word = word.substring(0,word.length() - 3) + "y";
				return word;
			}
		}

		if(word.endsWith("es")){
			if(!word.endsWith("aes") && !word.endsWith("ees") && !word.endsWith("oes")){
				word = word.substring(0,word.length() - 2) + "s";
				return word;
			}
		}

		if(word.endsWith("s")){
			if(!word.endsWith("us") && !word.endsWith("ss")){
				word = word.substring(0,word.length() - 1) + "";
				return word;
			}
		}

        return word;
    }

    public static void main(String[] args){

        try {
            String line = args[0];
            System.out.println(stem(line));
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

} 
