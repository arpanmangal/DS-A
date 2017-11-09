import java.util.*;

public class check {

    public static void main(String args[]) {
        Scanner s = new Scanner(System.in);
        ArrayList<String> arr = new ArrayList<>();
        while (s.hasNextInt()) {
            arr.add(s.next());
        }
        Collections.sort(arr);
        for (int i = 0; i < arr.size(); i++) {
            System.out.println(arr.get(i));
        }
    }
}