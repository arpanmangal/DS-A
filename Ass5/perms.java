import java.util.*;

public class perms {

    // char perm[][];
    // public String[] getPerms() {
    //     String[] per = new String[40320];
    //     perm = new char[40320][9];
    //     for (int i = 0; i < 9; i++) {
    //         int val = 0;
    //         for (int j = 0; j < 40320;) {
    //             for (int m = 1; m <= i + 1; m++) {
    //                 perm[j][i] = (char)('0' + val);
    //                 j++;
    //             }
    //             val = (val + 1) % 9;
    //         }
    //     }
    //     for (int i = 0; i < 40320; i++) {
    //         per[i] = new String(perm[i]);
    //     }
    //     return per; 
    // }
    ArrayList<String> perms;
    public  void permutation(String str) { 
        permutation("", str); 
    }

    private  void permutation(String prefix, String str) {
        int n = str.length();
        if (n == 0) perms.add(prefix);
        else {
            for (int i = 0; i < n; i++)
                permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
        }
    }

    public static void main(String args[]) {
        perms perm = new perms();
        // String[] perms = perm.getPerms();
        // for (int i = 0; i < perms.length; i++) {
        //     System.out.println(perms[i]);
        // }
        perm.perms = new ArrayList<>();
        perm.permutation("012345678");
        for (int i = 0; i < 362880; i++) System.out.println(perm.perms.get(i));
    }
}