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
            for (int i = 1; i < 38; i++) bucket[i] = 0; // don't consider space
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
        public bucket getDiff(String s) {
            // returns s - this
            bucket diff = new bucket();
            diff.word = "";
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == ' ') diff.bucket[0]++;
                else if (s.charAt(i) == 39) diff.bucket[1]++;
                else if (s.charAt(i) >= 48 && s.charAt(i) <= 57) diff.bucket[s.charAt(i) - 46]++; //s.charAt(i) - 48 + 2, numbers
                else if (s.charAt(i) >= 97 && s.charAt(i) <= 122) diff.bucket[s.charAt(i) - 85]++; //s.at(i) - 97 + 12, numbers
                else {
                    // illegal letter
                    return null; // hash unsuccessful
                }
            }
            for (int i = 1; i <= 1; i++) 
            {
                diff.bucket[i] -= this.bucket[i]; // don't consider space !!!
                if (diff.bucket[i] < 0) {
                    return null;
                }
                for(int j = 0; j < diff.bucket[i]; j++) {
                    // add the letter diff.bucket[i] times
                        diff.word += (char)(39);
                }
            }
            for (int i = 2; i < 12; i++) 
            {
                diff.bucket[i] -= this.bucket[i]; // don't consider space !!!
                if (diff.bucket[i] < 0) {
                    return null;
                }
                for(int j = 0; j < diff.bucket[i]; j++) {
                    // add the letter diff.bucket[i] times
                        diff.word += (char)(i + 46);
                }
            }
            for (int i = 12; i < 38; i++) 
            {
                diff.bucket[i] -= this.bucket[i]; // don't consider space !!!
                if (diff.bucket[i] < 0) {
                    return null;
                }
                for(int j = 0; j < diff.bucket[i]; j++) {
                    // add the letter diff.bucket[i] times
                        diff.word += (char)(i + 85);
                }
            }
            // System.out.println("before return: "+diff.word);
            return diff;
        }
    }

    private class pair {
        public String prefix;
        public String suffix;

        // constructor
        public pair(String p, String s) {
            prefix = p;
            suffix = s;
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
        if (s.length() > 12) return anagrams; // more than 12 letter word

        // get no space anagrams
        anagrams.addAll(getFirstOrderAngrms(s));
        // get one space anagrams
        if (s.length() >= 6) anagrams.addAll(getSecondOrderAngrms(s));

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

    private ArrayList<String> getSecondOrderAngrms(String s) {
        // returns all 1 space anagrams of s
        // System.out.println("in get 2nd order: s = "+s);
        ArrayList<String> anagrams = new ArrayList<>();
        bucket hash = new bucket(s);
        int size = s.length();

        if (size > 12 || !hash.hash()) return anagrams; // long word or unsuccesful hash => invalid word
        
        /*for (int i = 3; i <= size - 3; i++) {
            // i is the prefix size
            preSufArr = getPreSuffPairs(i, s);
            for (int j = 0; j < preSufArr.size(); j++) {
                // check if valid
                if (checkAnaPair())
            }
        }*/

        // approach => for each word in dictionary, subtract it from current word, and prepend it to the anagrams of remaining word
        for (int p = 3; p <= size - 3; p++) {
            // iterate the vocab with str.len
            // System.out.println(p);
            ArrayList<bucket> buckets = words[p - 3];
            for (int i = 0; i < buckets.size(); i++) {
                // buckets.get(i).print();
                // hash.print();
                // System.out.println(buckets.get(i).word);
                // prefix is buckets.get(i).word
                bucket suffix = buckets.get(i).getDiff(s);
                if (suffix == null) {
                    // difference not possible, prev not part of s
                    continue;
                }
                // System.out.println("Prefix: "+buckets.get(i).word+" Suffix: "+suffix.word);
                ArrayList<String> suffAngm = new ArrayList<>();
                suffAngm.addAll(getFirstOrderAngrms(suffix.word));
                for (int j = 0; j < suffAngm.size(); j++) {
                    suffAngm.set(j, buckets.get(i).word + " " + suffAngm.get(j));
                }
                anagrams.addAll(suffAngm);
            }
        }
        return anagrams;
    }

    /*private ArrayList<pair> getPreSuffPairs(int prfLen, String s) {
        ArrayList<pair> preSufArr = new ArrayList<>();

        // termination
        if (prfLen == 0) {
            preSufArr.add(new pair("", s)); // empty prefix and suffix as the same string
            return preSufArr;
        }
        ArrayList<pair> tmp;// = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            // ith element of the string as first letter of prefix
            tmp = getPreSuffPairs(prfLen - 1, s.substring(i + 1)); // gets the prefix, suffix pairs of substr(i+1, end) recursively => could be optimised with dp
            
            for (int j = 0; j < tmp.size(); j++) {
                // append the first letter
                // System.out.println("Debug: "+tmp.get(j).prefix+tmp.get(j).suffix);
                tmp.get(j).prefix = s.charAt(i) + tmp.get(j).prefix;
                tmp.get(j).suffix = s.substring(0, i) + tmp.get(j).suffix;
            }
            preSufArr.addAll(tmp);
        }
        return preSufArr;
    }*/
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


        // testing prefix-suffix
     /*   ArrayList<pair> test = findAna.getPreSuffPairs(6, "ghypios");
        for (int i = 0; i < test.size(); i++) {
            System.out.println(test.get(i).prefix + " " + test.get(i).suffix);
        }*/
        long time=System.currentTimeMillis()-startTime;
        System.out.println("time: "+time+" millis");
    }
}