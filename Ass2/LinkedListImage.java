// works on the idea of linked lists
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




public class LinkedListImage implements CompressedImageInterface {
    
    private class cell {
        // implements column node for a linked list
        int data;
        boolean start; // true for start false for end of black pixel streak
        cell next;
        cell()
        {
            this(-1, false); // -1 for no value
        }
        cell(int dataInput, boolean startInput)
        {
            data = dataInput;
            start = startInput;
            next = null;
        }
    }

    private class row {
        // implements row node for a singly linked list
        cell start;
        row next;
        row() {
            start = null;
            next = null;
        }
    }

    private row rowHead; // reference to linked list storing rows
    private int numRows, numCols; // number of rows and cols of the image
    
    /* All the below is done to remove the redundancy in the constructors
        Both the constructors have essentially the same code except the one has
        to take its input from a file. We call one constructor in other but it leads
        to problems due to compiler insisting that this(); be the first statement.
        So other helper functions have been used. */

    // a class to return multiple values as needed
    private class constructorArgs
    {
        boolean[][] grid;
        int rows;
        int cols;
        constructorArgs(boolean[][] grid, int rows, int cols) {
            this.grid = grid;
            this.rows = rows;
            this.cols = cols;
        }
    }
    
    // private general constructor
    private LinkedListImage()
    {
        rowHead = null;
        numRows = 0;
        numCols = 0;
    }

    // helper function to read file and return the required data
    private constructorArgs takeFileInput(String filename)
    {
        // take input from the file
        Scanner s = null;
        try
        {
            s = new Scanner(new File(filename));
        }
        catch(IOException e)
        {
            System.out.println(e);
            System.exit(1); // terminate the program
        }
        int numRows = s.nextInt();
        int numCols = s.nextInt();
        boolean[][] grid = new boolean[numRows][numCols];
        int tmp;
        for (int i = 0; i < numRows; i++)
        {
            for (int j = 0; j < numCols; j++)
            {
                tmp = s.nextInt();
                if (tmp == 0) grid[i][j] = false;
                else grid[i][j] = true;
            }
        }
        return new constructorArgs(grid, numRows, numCols);
    }
 
    // required constructor
	public LinkedListImage(String filename)
	{
        this();
        constructorArgs args = takeFileInput(filename);
        generateCompressedForm(args.grid, args.rows, args.cols);
	}
    // required constructor
    public LinkedListImage(boolean[][] grid, int width, int height)
    {
        this();
        generateCompressedForm(grid, width, height);
    }

    /* heleper (main) function to generate the compressed form
       implements a singly linked list for the rows
       each node of this list has a pointer to the doubly linked list of cells aka 
       various columns of the row. */
    private void generateCompressedForm(boolean[][] grid, int width, int height)
    {
		numRows = width;
        numCols = height;

        row currentRow = new row(); // reference to current Row
        rowHead = currentRow;

        // if either of numRows or numCols is zero is would lead to a one element linked list
        // in that dimention and automatically checks for emptyness. No need to check it explicitly.
        for (int i = 0; i < numRows; i++) {
            // the pointer of the column linked list is the currentRow
            cell currentCell = new cell(-1, false); // reference to current cell, the first cell set to -1 value;
            currentRow.start = currentCell;
            int state = 0; // 0 or 1 depending on which streak black or white is comming
            int tmpInput;
            for (int j = 0; j < numCols; j++) {
                // take the input
                tmpInput = (grid[i][j] == false) ? 0 : 1;
                if (state == 0) {
                    // currently white streak
                    if (tmpInput == 1) continue;
                    else
                    {
                        // start a black streak
                        currentCell.next = new cell(j, true);
                        state = 1;
                        currentCell = currentCell.next;
                        if (j == numCols - 1) {
                            // also end the black streak
                            currentCell.next = new cell(j, false);
                            state = 1;
                            currentCell = currentCell.next;
                        }
                    }
                } else {
                    // state = 1
                    // currently black streak
                    if (tmpInput == 0 && j < numCols - 1) continue;
                    else
                    {
                        // end the black streak
                        if (tmpInput == 0) currentCell.next = new cell(j, false);
                        else currentCell.next = new cell(j - 1, false); // the input of column has ended with black
                        state = 0;
                        currentCell = currentCell.next;
                    }
                }
            }
            row tmp = new row(); // a new row
            currentRow.next = tmp; // connect the new row to our list
            currentRow = tmp; // change the current row
            /* one extra row will be created at the end but it would only have 2 pointers so no significant memory loss*/
        }
    }

    public boolean getPixelValue(int x, int y) throws PixelOutOfBoundException
    {
		if (x >= numRows || x < 0 || y >= numCols || y < 0) {
            throw new PixelOutOfBoundException("Asked Pixel value out of Bounds.");
        }

        row currentRow = rowHead;
        // traverse upto the required row
        while(x-- > 0) {
            if (currentRow == null) {
                throw new java.lang.NullPointerException(" x = " + x);
            }
            currentRow = currentRow.next;
        }
        // traverse to the cell
        cell currentCell = currentRow.start.next;
        while(true) {
            if (currentCell == null) {
                // means all are white
                // or remaining white
                return true;
            }
            if (currentCell.data == y)
            {
                // we got our cell
                // it is either start or end of a black streak
                return false;
            } else if (currentCell.data > y) {
                // current cell has exceeded y
                if (currentCell.start == true)
                {
                    // y is behind black streak
                    return true;
                }
                else return false; // black
            } else {
                // continue
                currentCell = currentCell.next;
            }
        }
    }

    public void setPixelValue(int x, int y, boolean val) throws PixelOutOfBoundException
    {
		if (x >= numRows || x < 0 || y >= numCols || y < 0) {
            throw new PixelOutOfBoundException("Asked Pixel value out of Bounds.");
        }

        row currentRow = rowHead;
        // traverse upto the required row
        while(x-- > 0) {
            if (currentRow == null) {
                throw new java.lang.NullPointerException(" x = " + x);
            }
            currentRow = currentRow.next;
        }
        // traverse to the cell
        cell currentCell = currentRow.start.next;
        cell prevCell = currentRow.start;
        cell prevPrevCell = currentRow.start;
        while(true) {
            if (currentCell == null) {
                // means all are white
                // or remaining white
                if (val == true) {
                    // already white so do nothing
                    return;
                } else {
                    // make it a black cell
                    if (y - prevCell.data == 1) {
                        // black cell adjacent to previous streak
                        if (prevPrevCell != prevCell) {
                            prevCell.data++;
                        } else {
                            // first to be black cell
                            prevCell.next = new cell(y, true);
                            prevCell.next.next = new cell(y, false);
                        }
                    } else {
                        // some far away cell
                        prevCell.next = new cell(y, true);
                        prevCell.next.next = new cell(y, false);
                    }
                    
                    return;
                }
            }
            if (currentCell.data == y)
            {
                // we got our cell
                // it is either start or end of a black streak
                if (val == true) {
                    // have to make it white
                    if (currentCell.start == true) {
                        // start of black streak
                        if (currentCell.data != currentCell.next.data) {
                            // black spans over more than 1 px
                            currentCell.data++;
                        } else {
                            // its a lone black pixel => boom it
                            prevCell.next = currentCell.next.next;
                            currentCell = prevCell.next;
                        }
                    } else {
                        // end of black streak
                        if (prevCell.data != currentCell.data) {
                            // black spans over more than 1px
                            currentCell.data--;
                        } else {
                            // its a lone black pixel => boom it
                            // case already covered before
                        }
                    }
                } else {
                    // have to make it black
                    // already a black cell
                }
                // our job is done
                return;
            }
            else if (currentCell.data > y)
            {
                // current cell has exceeded y
                if (val == true) {
                    // have to make cell y as white
                    if (currentCell.start == true) {
                        // already white => do nothing
                    } else {
                        // break the black streak
                        cell tmp1 = new cell(y - 1, false);
                        cell tmp2 = new cell(y + 1, true);
                        prevCell.next = tmp1;
                        tmp1.next = tmp2;
                        tmp2.next = currentCell;
                    }
                } else {
                    // have to make cell y as black
                    if (currentCell.start == false) {
                        // already black => do nothing
                    } else {
                        // add a black node
                        if (currentCell.data - prevCell.data == 2) {
                            // there is only one white cell present
                            if (prevPrevCell == prevCell) {
                                // its the first white cell
                                currentCell.data--;
                            } else {
                                // bridge the gap
                                prevPrevCell.next = currentCell.next;
                            }
                        } else if (currentCell.data == y + 1) {
                            // make the just prev pixel black
                            currentCell.data--;
                        } else if (prevCell.data == y -1) {
                            // make the just after pixel black
                            if(prevPrevCell != prevCell) {
                                prevCell.data++;
                            } else {
                                // arrange for new black streak
                                cell tmp1 = new cell(y, true);
                                cell tmp2 = new cell(y, false);
                                prevCell.next = tmp1;
                                tmp1.next = tmp2;
                                tmp2.next = currentCell;
                            }
                        } else {
                            // arrange for new black streak
                            cell tmp1 = new cell(y, true);
                            cell tmp2 = new cell(y, false);
                            prevCell.next = tmp1;
                            tmp1.next = tmp2;
                            tmp2.next = currentCell;
                        }
                    }
                }
                // our job is done
                return;
            }
            else
            {
                // continue
                currentCell = currentCell.next;
                if (prevPrevCell != prevCell) {
                    prevPrevCell = prevPrevCell.next;
                }
                prevCell = prevCell.next;
            }
        }
    }

    public int[] numberOfBlackPixels()
    {
		int[] arr = new int[numRows];
        row currentRow = rowHead;
        int index = 0;
        while(currentRow.next != null) {
            int blackPixels = 0;
            cell currentCell = currentRow.start.next;
            while(currentCell != null) {
                if (currentCell.start) {
                    // start of black pixels
                    blackPixels -= currentCell.data;
                } else {
                    // end of black pixels
                    blackPixels += currentCell.data + 1;
                }
                currentCell = currentCell.next;
            }
            arr[index] = blackPixels;
            index++;
            currentRow = currentRow.next;
        }
        return arr;
    }
    
    public void invert() 
    {  
        // iterate over the rows and columns
        row currentRow = rowHead;
        if (rowHead != null) while(currentRow.next != null) {
            // traverse the cells of the row
            cell currentCell = currentRow.start.next; // the first cell initially
            cell prevCell = currentRow.start; // initially that with data = -1

            // first cell processing
            if (currentCell == null) {
                // all white pixels
                prevCell.next = new cell(0, true);
            } else if (currentCell.data == 0) {
                // black is from start
                // boom this cell
                prevCell.next = currentCell.next;
                currentCell = currentCell.next;
            } else {
                // initially some white pixels
                cell tmp = new cell(0, true);
                currentCell.data -= 1;
                currentCell.start = false;
                prevCell.next = tmp;
                tmp.next = currentCell;

                // go ahead
                prevCell = currentCell;
                currentCell = currentCell.next;
            }

            // process other cells
            while (currentCell != null) {
                if (currentCell.start == false) {
                    // end of black -> make it start of white
                    currentCell.data += 1;
                    currentCell.start = true;
                } else {
                    // start of black -> make it end of white
                    currentCell.data -= 1;
                    currentCell.start = false;
                }

                // go ahead
                currentCell = currentCell.next;
                if (currentCell != null) prevCell = prevCell.next;
            }

            // check for the last pixels
            if (prevCell.next.data == numCols) {
                // we have got a bit far away and there is no black pixel to be made
                prevCell.next = null;
            } else {
                // its the start of last black pixels
                prevCell = prevCell.next;
                prevCell.next = new cell(numCols - 1, false);
            }

            // go to next row
            currentRow = currentRow.next;
        }
    }
    

    private cell gernerateOrderedCells(row currentRow1, row currentRow2)
    {
        // traverse the cells of the row
        cell ptr1 = currentRow1.start.next; // the first cell;
        cell ptr2 = currentRow2.start.next; // the first cell;
        cell orderedCells = new cell(-1, false); // this will be the head of the new linked list
        cell currentCell = orderedCells;
        while (ptr1 != null && ptr2 != null)
        {
            // arrange in ascending order
            if (ptr1.data > ptr2.data) {
                currentCell.next = new cell(ptr2.data, ptr2.start);
                ptr2 = ptr2.next;
            } else if (ptr1.data < ptr2.data) {
                currentCell.next =  new cell(ptr1.data, ptr1.start);;
                ptr1 = ptr1.next;
            } else {
                // the one which has start = true is given priority
                // both are symmetrical if start is identical
                if (ptr1.start == true) {
                    currentCell.next =  new cell(ptr1.data, ptr1.start);;
                    ptr1 = ptr1.next;
                    currentCell = currentCell.next;
                    currentCell.next = new cell(ptr2.data, ptr2.start);
                    ptr2 = ptr2.next;
                } else {
                    currentCell.next = new cell(ptr2.data, ptr2.start);
                    ptr2 = ptr2.next;
                    currentCell = currentCell.next;
                    currentCell.next =  new cell(ptr1.data, ptr1.start);;
                    ptr1 = ptr1.next;
                }
            }
            // move ahead
            currentCell = currentCell.next;
        }
        if (ptr1 != null && ptr2 == null)
        {
             // apend the remaining list
            while(ptr1 != null) {
                currentCell.next = new cell(ptr1.data, ptr1.start);
                ptr1 = ptr1.next;
                currentCell = currentCell.next;
            }
        } else {
            // apend the remaining list of ptr2
            while(ptr2 != null) {
                currentCell.next = new cell(ptr2.data, ptr2.start);
                ptr2 = ptr2.next;
                currentCell = currentCell.next;
            }
        }
        return orderedCells;
    }

    public void performAnd(CompressedImageInterface img) throws BoundsMismatchException
    {
         // type casting img to an instance of LinkedListImage to perform various operations
        LinkedListImage img2 = (LinkedListImage) img;
		if (this.numRows != img2.numRows || this.numCols != img2.numCols) {
            // invalid input
            throw new BoundsMismatchException("Please give image of correct resolution");
        }

        // iterate over the row and column
        row currentRow1 = this.rowHead;
        row currentRow2 = img2.rowHead;
        if (this.rowHead != null) while(currentRow1.next != null)
        {
            cell orderedCells = gernerateOrderedCells(currentRow1, currentRow2);
            // begin the processing of the data received
            cell prevCell = orderedCells;
            cell currentCell = orderedCells.next;
            int s = 0; // s = 0, 1, 2; for AND s > 0
            while(currentCell != null)
            {
                if (currentCell.start == true) {
                    s += 1;
                    if (s == 1) {
                        // start new streak
                        // keep the node intact
                        currentCell = currentCell.next;
                        prevCell = prevCell.next;
                    } else {
                        // continue the streak
                        // no need of this node
                        currentCell = currentCell.next;
                        prevCell.next = currentCell;
                    }
                } else {
                    s -= 1;
                    if (s == 0) {
                        // check if there is a adjacent begining
                        if (currentCell.next != null && (currentCell.next.data - currentCell.data == 1) && currentCell.next.start) {
                            // continue the streak
                            // no need of this node and the next node
                            s += 1;
                            currentCell = currentCell.next.next;
                            prevCell.next = currentCell;
                        } else {
                            // end the streak
                            // keep the node intact
                            currentCell = currentCell.next;
                            prevCell = prevCell.next;
                        }
                    } else {
                        // continue the streak
                        // no need of this node
                        currentCell = currentCell.next;
                        prevCell.next = currentCell;
                    }
                }
            }
            // assign the updated linked list to the old one
            currentRow1.start = orderedCells;

            // to the next row
            currentRow1 = currentRow1.next;
            currentRow2 = currentRow2.next;
        }
        return;
    }
    
    public void performOr(CompressedImageInterface img) throws BoundsMismatchException
    {
         // type casting img to an instance of LinkedListImage to perform various operations
        LinkedListImage img2 = (LinkedListImage) img;
		if (this.numRows != img2.numRows || this.numCols != img2.numCols) {
            // invalid input
            throw new BoundsMismatchException("Please give image of correct resolution");
        }

        // iterate over the row and column
        row currentRow1 = this.rowHead;
        row currentRow2 = img2.rowHead;
        if (this.rowHead != null) while(currentRow1.next != null)
        {
            cell orderedCells = gernerateOrderedCells(currentRow1, currentRow2);
            // begin the processing of the data received
            cell prevCell = orderedCells;
            cell currentCell = orderedCells.next;
            int s = 0; // s = 0, 1, 2; for OR s < 2
            while(currentCell != null)
            {
                if (currentCell.start == true) {
                    s += 1;
                    if (s == 2) {
                        // start the streak
                        // keep the node intact
                        currentCell = currentCell.next;
                        prevCell = prevCell.next;
                    } else {
                        // is not needed
                        currentCell = currentCell.next;
                        prevCell.next = currentCell;
                    }
                } else {
                    s -= 1;
                    if (prevCell.start == true) {
                        // end the streak
                        // keep the node intact
                        currentCell = currentCell.next;
                        prevCell = prevCell.next;
                    } else {
                        // no need of this node
                        currentCell = currentCell.next;
                        prevCell.next = currentCell;
                    }
                }
            }
            // assign the updated linked list to the old one
            currentRow1.start = orderedCells;

            // to the next row
            currentRow1 = currentRow1.next;
            currentRow2 = currentRow2.next;
        }
        return;
    }
    
    public void performXor(CompressedImageInterface img) throws BoundsMismatchException
    {
         // type casting img to an instance of LinkedListImage to perform various operations
        LinkedListImage img2 = (LinkedListImage) img;
		if (this.numRows != img2.numRows || this.numCols != img2.numCols) {
            // invalid input
            throw new BoundsMismatchException("Please give image of correct resolution");
        }

        // iterate over the row and column
        row currentRow1 = this.rowHead;
        row currentRow2 = img2.rowHead;
        if (this.rowHead != null) while(currentRow1.next != null)
        {
            cell orderedCells = gernerateOrderedCells(currentRow1, currentRow2);
            // begin the processing of the data received
            cell prevCell = orderedCells;
            cell currentCell = orderedCells.next;
           
            boolean streak = false; // true if black streak is going on
            cell result = new cell(-1, false);
            cell index = result;
            cell indexPrev = result;
            
            if (currentCell == null)
            {
                // both were bright arrays xor = 0
                index.next = new cell(0, true);
                index = index.next;
                index.next = new cell(numCols - 1, false);
                index = index.next;
            } else if (currentCell.data != 0) {
                // you have to start an initial streak
                index.next = new cell(0, true);
                if (index != indexPrev) indexPrev = indexPrev.next;
                index = index.next;
                streak = true;
            } else {
                // current cell is the begining of black in atleast one of them
                if (currentCell.next != null && currentCell.next.data == currentCell.data && currentCell.next.start) {
                    // both black -> start the streak
                    streak = true;
                    index.next = new cell(0, true);
                    if (index != indexPrev) indexPrev = indexPrev.next;
                    index = index.next;
                    // throw away both the cells
                    currentCell = currentCell.next.next;
                } else {
                    // only one has black
                    streak = false;
                    // this cell has been processed
                    currentCell = currentCell.next;
                }
            }
            while(currentCell != null)
            {
                if (currentCell.start) {
                    if (currentCell.next != null && currentCell.next.data == currentCell.data && currentCell.next.start) {
                        // both black streaks have begun simultaneouly
                        // no need to terminate existing streak = 1
                        currentCell = currentCell.next.next;
                    }
                    else if (streak) {
                        // currently both are going white but black wants to start => finish the streak if continuing
                        streak = false;
                        if (index.data <= currentCell.data - 1) {
                            index.next = new cell(currentCell.data - 1, false);
                            if (index != indexPrev) indexPrev = indexPrev.next;
                            index = index.next;
                            currentCell = currentCell.next;
                        } else {
                            // means this is not a actual streak
                            // restore our index
                            index = indexPrev;
                            currentCell = currentCell.next;
                        }
                    } else {
                        // both black starts
                        streak = true;
                        index.next = new cell(currentCell.data, true);
                        if (index != indexPrev) indexPrev = indexPrev.next;
                        index = index.next;
                        currentCell = currentCell.next;
                    }
                } else {
                    if (currentCell.next != null && currentCell.next.data == currentCell.data && !currentCell.next.start) {
                        // both were black and now will be white
                        // no need to break the current streak = 1
                        currentCell = currentCell.next.next;
                    } else if (streak) {
                        // both were black end of one of them
                        streak = false;
                        index.next = new cell(currentCell.data, false);
                        if (index != indexPrev) indexPrev = indexPrev.next;
                        index = index.next;
                        currentCell = currentCell.next;
                    } else {
                        // one was black -> its end
                        if (currentCell.data  == numCols - 1) {
                            // its the end
                            // no use of starting any new streaks
                            index.next = null;
                            streak = false;
                            currentCell = currentCell.next;
                        } else {
                            // space is there for new streak
                            streak = true;
                            index.next = new cell(currentCell.data + 1, true);
                            if (index != indexPrev) indexPrev = indexPrev.next;
                            index = index.next;
                            currentCell = currentCell.next;
                        }
                    }
                }
            }
            if (index.start) {
                // finish the ongoing streak
                index.next = new cell(numCols - 1, false);
            }
            currentRow1.start = result;

            // to the next row
            currentRow1 = currentRow1.next;
            currentRow2 = currentRow2.next;
        }
        return;
    }

    public String toStringUnCompressed()
    {
		String ans = "";
        ans += numRows + " " + numCols + ",";
        // iterate over the row and column
        row currentRow = rowHead;
        if (rowHead != null) while(currentRow.next != null)
        {
            // traverse the cells of the row
            cell currentCell = currentRow.start.next; // the first cell;
            cell prevCell = currentRow.start; // the cell with data = -1
            int printed = 0;
            while (currentCell != null)
            {
                // add it to the string
                if (currentCell.start == false)
                {
                    // print zeros
                    int zeros = currentCell.data - prevCell.data + 1;
                    for (int i = 0; i < zeros; i++) ans += " 0";
                    printed += zeros;
                }
                else 
                {
                    // print ones
                    int ones = currentCell.data - prevCell.data - 1;
                    for (int i = 0; i < ones; i++) ans += " 1";
                    printed += ones;
                }
                prevCell = currentCell;
                currentCell = currentCell.next;
            }
             // the remaining are 1's
            for (int i = numCols - printed; i > 0; i--) ans += " 1";
            // end the row
            ans += ",";
            currentRow = currentRow.next;
        }
        return ans.substring(0, ans.length() - 1);
    }
    
    public String toStringCompressed()
    {
        String ans = "";
        ans += numRows + " " + numCols + ",";
        // iterate over the row and column
        row currentRow = rowHead;
        if (rowHead != null) while(currentRow.next != null)
        {
            // traverse the cells of the row
            cell currentCell = currentRow.start.next; // the first cell;
            //int status = 0;
            while (currentCell != null)
            {
                // add it to the string as it is
                ans += " " + currentCell.data;
                currentCell = currentCell.next;
            }
            // end the row
            ans += " -1,";
            currentRow = currentRow.next;
        }
        return ans.substring(0, ans.length() - 1);
    }

    public static void main(String[] args) {
    	// testing all methods here :
    	boolean success = true;

    	// check constructor from file
    	CompressedImageInterface img1 = new LinkedListImage("sampleInputFile.txt");

    	// check toStringCompressed
    	String img1_compressed = img1.toStringCompressed();
    	String img_ans = "16 16, -1, 5 7 -1, 3 7 -1, 2 7 -1, 2 2 6 7 -1, 6 7 -1, 6 7 -1, 4 6 -1, 2 4 -1, 2 3 14 15 -1, 2 2 13 15 -1, 11 13 -1, 11 12 -1, 10 11 -1, 9 10 -1, 7 9 -1";
    	success = success && (img_ans.equals(img1_compressed));

    	if (!success)
    	{
    		System.out.println("Constructor (file) or toStringCompressed ERROR");
    		return;
    	}

    	// check getPixelValue
    	boolean[][] grid = new boolean[16][16];
    	for (int i = 0; i < 16; i++)
    		for (int j = 0; j < 16; j++)
    		{
                try
                {
        			grid[i][j] = img1.getPixelValue(i, j);                
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
    		}

    	// check constructor from grid
    	CompressedImageInterface img2 = new LinkedListImage(grid, 16, 16);
    	String img2_compressed = img2.toStringCompressed();
    	success = success && (img2_compressed.equals(img_ans));

    	if (!success)
    	{
    		System.out.println("Constructor (array) or toStringCompressed ERROR");
    		return;
    	}

    	// check Xor
        try
        {
        	img1.performXor(img2);       
        }
        catch (BoundsMismatchException e)
        {
            System.out.println("Errorrrrrrrr");
        }
    	for (int i = 0; i < 16; i++)
    		for (int j = 0; j < 16; j++)
    		{
                try
                {
        			success = success && (!img1.getPixelValue(i,j));                
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
    		}

    	if (!success)
    	{
    		System.out.println("performXor or getPixelValue ERROR");
    		return;
    	}

    	// check setPixelValue
    	for (int i = 0; i < 16; i++)
        {
            try
            {
    	    	img1.setPixelValue(i, 0, true);            
            }
            catch (PixelOutOfBoundException e)
            {
                System.out.println("Errorrrrrrrr");
            }
        }

    	// check numberOfBlackPixels
    	int[] img1_black = img1.numberOfBlackPixels();
    	success = success && (img1_black.length == 16);
    	for (int i = 0; i < 16 && success; i++)
    		success = success && (img1_black[i] == 15);
    	if (!success)
    	{
    		System.out.println("setPixelValue or numberOfBlackPixels ERROR");
    		return;
    	}

    	// check invert
        img1.invert();
        for (int i = 0; i < 16; i++)
        {
            try
            {
                success = success && !(img1.getPixelValue(i, 0));            
            }
            catch (PixelOutOfBoundException e)
            {
                System.out.println("Errorrrrrrrr");
            }
        }
        if (!success)
        {
            System.out.println("invert or getPixelValue ERROR");
            return;
        }

    	// check Or
        try
        {
            img1.performOr(img2);        
        }
        catch (BoundsMismatchException e)
        {
            System.out.println("Errorrrrrrrr");
        }
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
            {
                try
                {
                    success = success && img1.getPixelValue(i,j);
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
            }
        if (!success)
        {
            System.out.println("performOr or getPixelValue ERROR");
            return;
        }

        // check And
        try
        {
            img1.performAnd(img2);    
        }
        catch (BoundsMismatchException e)
        {
            System.out.println("Errorrrrrrrr");
        }
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
            {
                try
                {
                    success = success && (img1.getPixelValue(i,j) == img2.getPixelValue(i,j));             
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
            }
        if (!success)
        {
            System.out.println("performAnd or getPixelValue ERROR");
            return;
        }

    	// check toStringUnCompressed
        String img_ans_uncomp = "16 16, 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1, 1 1 1 1 1 0 0 0 1 1 1 1 1 1 1 1, 1 1 1 0 0 0 0 0 1 1 1 1 1 1 1 1, 1 1 0 0 0 0 0 0 1 1 1 1 1 1 1 1, 1 1 0 1 1 1 0 0 1 1 1 1 1 1 1 1, 1 1 1 1 1 1 0 0 1 1 1 1 1 1 1 1, 1 1 1 1 1 1 0 0 1 1 1 1 1 1 1 1, 1 1 1 1 0 0 0 1 1 1 1 1 1 1 1 1, 1 1 0 0 0 1 1 1 1 1 1 1 1 1 1 1, 1 1 0 0 1 1 1 1 1 1 1 1 1 1 0 0, 1 1 0 1 1 1 1 1 1 1 1 1 1 0 0 0, 1 1 1 1 1 1 1 1 1 1 1 0 0 0 1 1, 1 1 1 1 1 1 1 1 1 1 1 0 0 1 1 1, 1 1 1 1 1 1 1 1 1 1 0 0 1 1 1 1, 1 1 1 1 1 1 1 1 1 0 0 1 1 1 1 1, 1 1 1 1 1 1 1 0 0 0 1 1 1 1 1 1";
        success = success && (img1.toStringUnCompressed().equals(img_ans_uncomp)) && (img2.toStringUnCompressed().equals(img_ans_uncomp));

        if (!success)
        {
            System.out.println("toStringUnCompressed ERROR");
            return;
        }
        else
            System.out.println("ALL TESTS SUCCESSFUL! YAYY!");
    }
}
