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
            bucket = new byte[38]; // 0-[  == 32] 1-[' == 39], 2-11 for 0-9, 12-37 for a-z/, automatically initialises to zero
        }
        public bucket(String s) {
            bucket = new byte[38];
            this.word = s;
        }

        // functions
        public boolean hash() {
            // hashs the contained word into the bucket
            for (int i = 0; i < word.length(); i++) {
                // #optimisation -> probability of being a word is much more
                if (word.charAt(i) >= 97) bucket[word.charAt(i) - 85]++; //word.at(i) - 97 + 12, numbers
                else if (word.charAt(i) >= 48) bucket[word.charAt(i) - 46]++; //word.charAt(i) - 48 + 2, numbers
                else if (word.charAt(i) == 39) bucket[1]++;
                else bucket[0]++; // (word.charAt(i) == ' ')
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
        public bucket getDiff(bucket b) {
            // returns s - this
            bucket diff = new bucket();
            diff.word = "";
            for (int i = 1; i < 38; i++) {
                diff.bucket[i] = (byte)(b.bucket[i] - this.bucket[i]);
                if (diff.bucket[i] < 0) {
                    return null;
                }
            }

            for (int i = 1; i <= 1; i++) 
            {
                for(int j = 0; j < diff.bucket[i]; j++) {
                    // add the letter diff.bucket[i] times
                        diff.word += (char)(39);
                }
            }
            for (int i = 2; i < 12; i++) 
            {
                for(int j = 0; j < diff.bucket[i]; j++) {
                    // add the letter diff.bucket[i] times
                        diff.word += (char)(i + 46);
                }
            }
            for (int i = 12; i < 38; i++) 
            {
                for(int j = 0; j < diff.bucket[i]; j++) {
                    // add the letter diff.bucket[i] times
                        diff.word += (char)(i + 85);
                }
            }
            return diff;
        }
    }

    private class container {
        public ArrayList<bucket> container;

        public container() {
            container = new ArrayList<bucket>();
        }
    }

    private int hash3(bucket b) {
        // gives a secondary hash of a string according to its first 3 characters
        int hash = 0;
        // space->'0', "'"->'1',0-9->'2-11', a-z->'12-37'; 
        for (int i = 0; i < 38; i++) hash += b.bucket[i] * i; // silly mistake to keep it just b.bucket[i]
        return hash;//s.charAt(0) + s.charAt(1) + s.charAt(2); // 122 * 3 = 366 ~ 400
    }
    private int vocabSize;
    private ArrayList<container>[] words; // stores the vocab words according to their size

    @SuppressWarnings("unchecked")
    private void loadVocab(Scanner s) {
        vocabSize = s.nextInt();
        String in;
        words = new ArrayList[10]; // for storing words of size 3 to 12;
        int hash; // hash3(input)

        for (int i = 0; i < 10; i++) {
            words[i] = new ArrayList<container>(450);
            for (int j = 0; j < 450; j++) {
                words[i].add(new container());
            }
        }
        for (int i = 0; i < vocabSize; i++) {
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
            hash = hash3(cell); // returns index where to store this word
            words[in.length() - 3].get(hash).container.add(cell);
        }
    }

    private ArrayList<String> getAnagrams(String s) {
        ArrayList<String> anagrams = new ArrayList<>();

        // get no space anagrams
        bucket hash = new bucket(s);
        int size = s.length();

        if (size > 12 || size < 3 || !hash.hash()) return anagrams; // long word, short word or unsuccesful hash => invalid word
        getFirstOrderAngrms(hash, anagrams, size);
        // get one space anagrams
        if (size >= 6) getSecondOrderAngrms(hash, anagrams, size);
        if (size >= 9) getThirdOrderAngrms(hash, anagrams, size);
        Collections.sort(anagrams);
        ArrayList<String> sortedAna = new ArrayList<>();
        if (anagrams.size() > 0) sortedAna.add(anagrams.get(0));
        for (int i = 1; i < anagrams.size(); i++) {
            if (!(anagrams.get(i).equals(anagrams.get(i - 1)))) {
                sortedAna.add(anagrams.get(i));
            }
        }
        return sortedAna;
    }
    private void getFirstOrderAngrms(bucket hash, ArrayList<String> anagrams, int size) {
        // returns all 0 space anagrams of hash.word

        int hash3 = hash3(hash);
        container cont = words[size - 3].get(hash3);
        for (int i = 0; i < cont.container.size(); i++) {
            if (cont.container.get(i).equalTo(hash)) {
                // found an anagram
                anagrams.add(cont.container.get(i).word);
            }
        }
        return;
    }
    private void getSecondOrderAngrms(bucket hash, ArrayList<String> anagrams, int size) {

        // approach => for each word in dictionary, subtract it from current word, and prepend it to the anagrams of remaining word
        container cont;
        for (int p = 3; p <= size / 2; p++) {
            // iterate the vocab with str.len
          for (int c = 0; c < 450; c++) {
            cont = words[p - 3].get(c);
            for (int i = 0; i < cont.container.size(); i++) {
                bucket suffix = cont.container.get(i).getDiff(hash); // => change signature to getDiff(bucket)
                if (suffix == null) {
                    // difference not possible, prev not part of s
                    continue;
                }
                int j = anagrams.size();
                getFirstOrderAngrms(suffix, anagrams, size - p);
                // prepend prefix to suffix
                // add suffix prefix
                int f = anagrams.size();
                for (; j < f; j++) {
                    anagrams.add(anagrams.get(j) + " " + cont.container.get(i).word);
                    anagrams.set(j, cont.container.get(i).word + " " + anagrams.get(j));
                }
            }
          }
        }
        return;
    }
    private void getThirdOrderAngrms(bucket hash, ArrayList<String> anagrams, int size) {
        // returns all 1 space anagrams of hash.word

        // approach => for each word in dictionary, subtract it from current word, and prepend it to the anagrams of remaining word
        container cont;
        for (int p = 3; size - p >= 6; p++) {
          for (int c = 0; c < 450; c++) {
            cont = words[p - 3].get(c);
            // iterate the vocab with str.len
            for (int i = 0; i < cont.container.size(); i++) {
                // prefix is buckets.get(i).word
                bucket suffix = cont.container.get(i).getDiff(hash);
                if (suffix == null) {
                    // difference not possible, prev not part of s
                    continue;
                }
                int j = anagrams.size();
                getSecondOrderAngrms(suffix, anagrams, size - p);
                for (; j < anagrams.size(); j++) {
                    anagrams.set(j, cont.container.get(i).word + " " + anagrams.get(j));
                }
            }
          }
        }
        return;
    }

    private void printAnagrams(ArrayList<String> list) {
        // prints the elements of the anagram list
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        System.out.println("-1");
    }

    public static void main(String args[]) {
        // long startTime=System.currentTimeMillis();

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

        // long loadTime =System.currentTimeMillis()-startTime;
        // find anagrams of input strings
        int numInputs = input.nextInt();
        String in = "";
        for (int i = 0; i < numInputs; i++) {
            // anagrams.clear();
            in = input.next();
            findAna.printAnagrams(findAna.getAnagrams(in));
        }

        // for calculating time -> uncomment
        // long findTime=System.currentTimeMillis()-loadTime-startTime;
        // System.out.println("loadTime: "+loadTime+" millis and findTime: "+findTime+" millis and totalTime: "+(loadTime+findTime)+" milis");
    }
}