import java.io.*;
import java.util.*;


public class stemming {

    public static void main(String []args) {

    	boolean marker = false;

		String word = "skunks";

		if(word.endsWith("ies")){
			if(!word.endsWith("eies") && !word.endsWith("aies")){
				word = word.substring(0,word.length() - 3) + "y";
				System.out.println(word);
			}
		}

		if(word.endsWith("es")){
			if(!word.endsWith("aes") && !word.endsWith("ees") && !word.endsWith("oes")){
				word = word.substring(0,word.length() - 2) + "s";
				System.out.println(word);
			}
		}

		if(word.endsWith("s")){
			if(!word.endsWith("us") && !word.endsWith("ss")){
				word = word.substring(0,word.length() - 1) + "";
				System.out.println(word);
			}
		}
    }
} 
