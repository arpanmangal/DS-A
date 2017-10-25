// program to print all the anagrams of a given word
import java.util.*;
import java.io.*;
import java.util.ArrayList;

public class Anagram {

    private class bucket {
        // stores the hash code of a string in form of 37 containers along with the original string
        public byte[] bucket;
        public String word;

        // constructors
        public bucket() {
            bucket = new byte[38]; // 0-[  == 32] 1-[' == 39], 2-11 for 0-9, 12-37 for a-z/
            for (int i = 0; i < 38; i++) bucket[i] = 0;
        }
        public bucket(String s) {
            this();
            this.word = s;
        }

        // functions
        public boolean hash() {
            // hashs the contained word into the bucket
            // System.out.println("hashing " + word);
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
        public boolean equalTo(bucket b) {
            for (int i = 0; i < 38; i++) {
                if (this.bucket[i] != b.bucket[i])
                    return false;
            }
            return true;
        }
        public void print() {
            // prints the buckets, mainly for debugging
            String s = word + ": ";
            for (int i = 0; i < 38; i++) {
                s += bucket[i] + " ";
            }
            System.out.println(s);
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
            words[in.length() - 3].add(cell);
        }
    }

    private ArrayList<String> getAnagrams(String s) {
        ArrayList<String> anagrams = new ArrayList<>();

        // get no space anagrams
        anagrams.addAll(getFirstOrderAngrms(s));

        Collections.sort(anagrams);
        return anagrams;
    }
    private ArrayList<String> getFirstOrderAngrms(String s) {
        // returns all 0 space anagrams of s
        // System.out.println("in get 1st order");
        ArrayList<String> anagrams = new ArrayList<>();
        bucket hash = new bucket(s);
        int size = s.length();

        if (size > 12 || !hash.hash()) return anagrams; // long word or unsuccesful hash => invalid word
        
        ArrayList<bucket> buckets = words[size - 3];
        for (int i = 0; i < buckets.size(); i++) {
            // buckets.get(i).print();
            // hash.print();
            if (buckets.get(i).equalTo(hash)) {
                // found an anagram
                anagrams.add(buckets.get(i).word);
            }
        }
        return anagrams;
    }

    private void printAnagrams(ArrayList<String> list) {
        // prints the elements of the anagram list
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        System.out.println("-1");
    }

    public static void main(String args[]) {
        long startTime=System.currentTimeMillis();

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

        // load vocab
        findAna.loadVocab(vocab);

        // find anagrams of input strings
        int numInputs = input.nextInt();
        String in = "";
        ArrayList<String> anagrams;// = new ArrayList<>();
        for (int i = 0; i < numInputs; i++) {
            // anagrams.clear();
            in = input.next();
            findAna.printAnagrams(findAna.getAnagrams(in));
        }

        long time=System.currentTimeMillis()-startTime;
        System.out.println("time: "+time+" millis");
    }
}