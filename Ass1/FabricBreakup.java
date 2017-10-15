// This is the code for part A: Fabric Breakup
// Usage: 
 	//  compile: javac FabricBreakup.java
	// execute: java FabricBreakup infile > outfile

// works on the idea of stack (using arrays)
import java.util.*;
import java.io.*;

// A Class that represents use-defined expception (from http://www.geeksforgeeks.org/g-fact-32-user-defined-custom-exception-in-java/)
class CommandLineArgumentsException extends Exception
{
    public CommandLineArgumentsException(String s)
    {
        // Call constructor of parent Exception
        super(s);
    }
}

public class FabricBreakup {
    // the pile of clothes
    private int[] pile = new int[100000000]; // assuming max no of clothes in pile = 1000000000 the size of int
    private int top; // returns top cloth's index

    // the array which stores the maximums (favourite clothes) in the pile
    private int[] maximums = new int[100000000];
    private int maximumMaximum; // returns the greatest maximum which is the top element of the above array

    // array storing number of clothes which will fall when max is removed
    private int[] fallableClothes = new int[100000000];
    // its top element will be same as maximumMaximum so no need to make a top variable

    public static void main(String[] args) throws IOException {
		Scanner s = null;
		try
	    {
	    	if (args.length != 1 && args.length != 3) throw new CommandLineArgumentsException("Provide correct command line arguments!!");
	        s = new Scanner(new File(args[0])); // input from file
	    //	Scanner s = new Scanner (System.in); // input from stdin
	    }
	    catch (CommandLineArgumentsException e)
	    {
	    	System.out.println(e);
	    	System.out.println("Usage: java FabricBreakup infile > outfile OR java FabricBreakup infile"); // Inform the user about correct usage
	    	System.exit(1); // stop execution
	    }
	    catch (IOException e)  
	    {
	        // insert code to run when exception occurs
	        System.out.println(e);
	        System.out.println("Usage: java FabricBreakup infile > outfile OR java FabricBreakup infile"); // Inform the user about correct usage
	        System.exit(1); // stop execution
	    }

    	
    	int N = s.nextInt(); // total number of operations

    	// make the Fabric
    	FabricBreakup clothes = new FabricBreakup();
    	// make all the stacks empty
    	clothes.top = clothes.maximumMaximum = -1;

    	while(N-- > 0) // loop over all the operations
    	{
    		// For Debugging->
    /*		if(clothes.top!=-1)System.out.printf("top = %d, maximumMaximum = %d, maximums[maximumMaximum] = %d, pile[maximums[maximumMaximum] = %d *** ", clothes.top, clothes.maximumMaximum, 
    			clothes.maximums[clothes.maximumMaximum], clothes.pile[clothes.maximums[clothes.maximumMaximum]]);
    		for (int i = 0; i <= clothes.top; i++)
    		{
    			System.out.printf("%d ", clothes.pile[i]);
    		}
    		System.out.println(); */

    		// do the operations
    		int number_of_op = s.nextInt();
    		int actionId = s.nextInt(); // currently program doesn't handle wrong inputs and expects desired inputs
    		int likeness;

    		if (actionId == 1)
    		{
    			// ask for the likeness of next cloth
    			likeness = s.nextInt();

    			// add it to the pile
    			clothes.top++;
    			clothes.pile[clothes.top] = likeness;

    			// update the maximums array if need be
    			if (clothes.maximumMaximum == -1)
    			{
    				// then this cloth is the only element in the pile
    				clothes.maximumMaximum++;
    				clothes.maximums[clothes.maximumMaximum] = 0; // index of the greatest maximum element
    				clothes.fallableClothes[clothes.maximumMaximum] = 0; // since the only cloth is there no cloth will fall when it is removed
    			}
    			else if (clothes.pile[clothes.maximums[clothes.maximumMaximum]] > likeness)
    			{
    				// a less favourite cloth has been added
    				// it will never be chosen and bound to fall when the favourite one is chosen
    				clothes.fallableClothes[clothes.maximumMaximum]++;
    			}
    			else
    			{
    				// a cloth of higher or equal favouritism has been picked
    				// it will be chosen first above all others
    				clothes.maximumMaximum++;
    				clothes.maximums[clothes.maximumMaximum] = clothes.top; // index of greatest maximum cloth
    				clothes.fallableClothes[clothes.maximumMaximum] = 0; // no cloth will fall as of now as no cloth above it
     			}
    		}
    		else
    		{
    			// party time
    			if (clothes.maximumMaximum == -1)
    			{
    				// no element in the pile (same as top == 0)
                    System.out.println("" + number_of_op + " -1");
    			}
    			else
    			{
    				// take out the most favorite shirt
    				// its index is given by the index stored in maximumMaximum of maximums Array
    				// no of clothes which fall will be given by fallableClothes[maximumMaximum]
    				System.out.println("" + number_of_op + " " + clothes.fallableClothes[clothes.maximumMaximum]);

    				// reduce the pile
    				clothes.top = clothes.maximums[clothes.maximumMaximum] - 1; // cloth before the one just removed
    				// update bookKeepings
    				clothes.maximumMaximum--;
    			}
    		}
    	}
    }
}