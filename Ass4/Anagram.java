// program to print all the anagrams of a given word
import java.util.*;
import java.io.*;

public class Anagram {

    private class bucket {
        // stores the hash code of a string in form of 37 containers along with the original string
        public byte[] bucket;
        public String word;

        // constructors
        public bucket() {
            bucket = new byte[37]; // 0-[  == 32] 1-[' == 39], 2-11 for 0-9, 12-37 for a-z/
            for (int i = 0; i < 37; i++) bucket[i] = 0;
        }
        public bucket(String s) {
            this();
            this.word = s;
        }

        // functions
        public boolean hash() {
            // hashs the contained word into the bucket
            
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == ' ') bucket[0]++;
                else if (word.charAt(i) == 39) bucket[1]++;
                else if (word.charAt(i) >= 48 && word.charAt(i) <= 57) bucket[word.charAt(i) - 46]++; //word.charAt(i) - 48 + 2, numbers
                else if (word.charAt(i) >= 97 && word.charAt(i) <= 122) bucket[word.charAt(i) - 85]++; //word.at(i) - 97 + 12, numbers
                else {
                    // illegal letter
                    return false; // hash unsuccessful
                }
            }
            return true;
        }
        public void getDiff(bucket b) {
            // 
        }
    }


    private int vocabSize;
    private ArrayList<bucket>[] words; // stores the vocab words according to their size
    private void loadVocab(Scanner s) {
        vocabSize = s.nextInt();
        String in = "";
        words = new ArrayList[10]; // for storing words of size 3 to 12;
        for (int i = 0; i < 10; i++) words[i] = new ArrayList<bucket>();
        for (int i = 0; i < vocabSize; i++) {
            // take input and store it
            in = s.next();
            if (in.length() < 3 || in.length() > 12) {
                // ignore
                continue;
            }
            bucket cell = new bucket(in);
            if (!cell.hash()) {
                // illegal input
                continue;
            }
            // add to appropriate place
            words[in.length() - 2].add(cell);
        }
    }

    public static void main(String args[]) {
        if (args.length != 2) {
            // check input
            System.out.println("java Anagram vocabulary.txt input.txt");
            System.exit(1);
        }

        Scanner vocab = null;
        Scanner input = null;
		try
	    {
            vocab = new Scanner(new File(args[0])); // vocabulary.txt
            input = new Scanner(new File(args[1])); // input.txt
	    }
	    catch (IOException e)  
	    {
	        System.out.println(e);
	        System.exit(1); // exit
        }
        
        Anagram findAna = new Anagram(); // object of our class
        findAna.loadVocab(vocab);
    }
}