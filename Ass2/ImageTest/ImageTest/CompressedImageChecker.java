import java.io.*;
import java.util.Scanner;

public class CompressedImageChecker
{
	public static int numTest = 0;
	public static int numPassed = 0;
	public static void main(String[] args) throws IOException {
		String fileName = "ImageTestResult.txt";
		FileWriter fileWriter = new FileWriter(fileName);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		System.out.println("Starting the tests :");
		printWriter.println("TestNo\tResult\t\tTotalPass\tTestName");
		getPixelTest(printWriter);
		setPixelTest(printWriter);
		numberOfBlackTest(printWriter);
		invertTest(printWriter);
		performAndTest(printWriter);
		performOrTest(printWriter);
		performXorTest(printWriter);
		printWriter.close();
	}

	public static void printToFile(PrintWriter pw, int testNo, String status,String testName) {
		pw.printf("%d \t%s \t%d %s\n",testNo,status,numPassed,testName);
	}

	public static void fillGridFromFile(boolean[][] grid, String fname) throws IOException
	{
		Scanner scanner = new Scanner(new File(fname));
		int sz = scanner.nextInt();
		sz = scanner.nextInt();
		// grid = new boolean[sz][scanner.nextInt()];
		for (int i = 0; i < sz; i++)
			for (int j = 0; j < sz; j++)
				grid[i][j] = (scanner.nextInt() == 1);
	}

	public static void getPixelTest(PrintWriter pw)
	{
		numTest++;
		boolean pass = true;
		String ans = "Pass";
		try
		{
			CompressedImageInterface img1 = new LinkedListImage("test_1_1.txt");
			CompressedImageInterface img10 = new LinkedListImage("test_10_1.txt");
			Scanner scanner = new Scanner(new File("test_10_1.txt"));
			boolean[][] img10_grid = new boolean[scanner.nextInt()][scanner.nextInt()];
			pass = pass && (!img1.getPixelValue(0, 0));
			for (int i = 0; i < 10; i++)
			{
				for (int j = 0; j < 10; j++)
				{
					img10_grid[i][j] = (scanner.nextInt() == 1);
					pass = pass && (img10.getPixelValue(i, j) == img10_grid[i][j]);
				}
			}
		}
		catch (Exception e)
		{
			pass = false;
		}
		try
		{
			CompressedImageInterface img10 = new LinkedListImage("test_10_1.txt");
			boolean x = img10.getPixelValue(9, 11);
			pass = false;
		}
		catch (CompressedImageInterface.PixelOutOfBoundException pe)
		{
		}
		catch (Exception e)
		{
			pass = false;
		}

		if (pass)
			numPassed++;
		else
			ans = "Fail";
		printToFile(pw, numTest, ans, "getPixelValue for test_1_1, test_10_1");
	}

	public static void setPixelTest(PrintWriter pw)
	{
		numTest++;
		boolean pass = true;
		String ans = "Pass";
		try
		{
			CompressedImageInterface img10_1 = new LinkedListImage("test_10_3.txt");
			CompressedImageInterface img10_2 = new LinkedListImage("test_10_3.txt");
			CompressedImageInterface img10_3 = new LinkedListImage("test_10_3.txt");
			String correct_unc = "10 10, ";
			String correct_unc_2 = "10 10, ";
			String correct_unc_3 = "10 10, ";
			for (int i = 0; i < 10; i++)
			{
				img10_1.setPixelValue(i, 0, true);
				img10_2.setPixelValue(i, 9, true);
				img10_3.setPixelValue(i, 5, true);
				correct_unc += "1 9 -1";
				correct_unc_2 += "0 8 -1";
				correct_unc_3 += "0 4 6 9 -1";
				if (i != 9)
				{
					correct_unc += ", ";
					correct_unc_2 += ", ";
					correct_unc_3 += ", ";
				}
			}
			String ans1 = img10_1.toStringCompressed();
			String ans2 = img10_2.toStringCompressed();
			String ans3 = img10_3.toStringCompressed();
			// System.out.println("Correct is :" + correct_unc + ", ans : " + ans1);
			// System.out.println("ans 2 : " + ans2);
			// System.out.println("ans 3 : " + ans3);
			pass = pass && (correct_unc.equals(ans1));
			pass = pass && (correct_unc_2.equals(ans2));
			pass = pass && (correct_unc_3.equals(ans3));
		}
		catch (Exception e)
		{
			pass = false;
		}
		if (pass)
			numPassed++;
		else
			ans = "Fail";
		printToFile(pw, numTest, ans, "setPixelValue for test_10_3 three times");
	}

	public static void numberOfBlackTest(PrintWriter pw)
	{
		numTest++;
		boolean pass = true;
		String ans = "Pass";
		try
		{
			CompressedImageInterface img10_1 = new LinkedListImage("test_10_1.txt");
			CompressedImageInterface img10_2 = new LinkedListImage("test_10_3.txt");
			CompressedImageInterface img10_3 = new LinkedListImage("test_10_4.txt");
			int[] numBlack1 = img10_1.numberOfBlackPixels();
			int[] numBlack2 = img10_2.numberOfBlackPixels();
			int[] numBlack3 = img10_3.numberOfBlackPixels();
			pass = pass && (numBlack1.length == 10) && (numBlack3.length == 10) && (numBlack2.length == 10);
			if (pass)
			{
				for (int i = 0; i < 10; i++)
					pass = pass && (numBlack2[i] == 10) && (numBlack3[i] == 0);
				for (int i = 0; i < 6; i++)
					pass = pass && (numBlack1[i] == 5);
				for (int i = 6; i < 10; i++)
					pass = pass && (numBlack1[i] == (10*((i+1)%2)));
			}
		}
		catch (Exception e)
		{
			pass = false;
		}
		if (pass)
			numPassed++;
		else
			ans = "Fail";
		printToFile(pw, numTest, ans, "numberOfBlackPixels for test_10_3, test_10_4, test_10_1");
	}

	public static void invertGrid(boolean[][] grid)
	{
		int sz = grid.length;
		for (int i = 0; i < sz; i++)
			for (int j = 0; j < sz; j++)
				grid[i][j] = !(grid[i][j]);
	}

	public static String gridToString(boolean[][] grid)
	{
		int sz = grid.length;
		String ans = sz + " " + sz + ", ";
		for (int i = 0; i < sz; i++)
		{
			int start = 0;
			int end = 0;
			while (end < sz)
			{
				while (start < sz && grid[i][start])
					start++;
				end = start;
				while (end < sz && (!grid[i][end]))
					end++;
				if (start != sz)
					ans += (start + " " + (end-1) + " ");
				start = end;
			}
			ans += "-1";
			if (i != (sz-1)) ans += ", ";
		}
		return ans;
	}

	public static void invertTest(PrintWriter pw)
	{
		numTest++;
		boolean pass = true;
		String ans = "Pass";
		try
		{
			CompressedImageInterface img10_1 = new LinkedListImage("test_10_1.txt");
			CompressedImageInterface img10_2 = new LinkedListImage("test_10_2.txt");
			boolean[][] img1 = new boolean[10][10];
			boolean[][] img2 = new boolean[10][10];
			fillGridFromFile(img1, "test_10_1.txt");
			fillGridFromFile(img2, "test_10_2.txt");
			img10_1.invert();
			img10_2.invert();
			invertGrid(img1);
			invertGrid(img2);
			String ans1 = gridToString(img1);
			String ans2 = gridToString(img2);
			pass = pass && (ans1.equals(img10_1.toStringCompressed())) && (ans2.equals(img10_2.toStringCompressed()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e);
			pass = false;
		}
		if (pass)
			numPassed++;
		else
			ans = "Fail";
		printToFile(pw, numTest, ans, "invert for test_10_1, test_10_2");		
	}

	public static void performAndTest(PrintWriter pw)
	{
		numTest++;
		boolean pass = true;
		String ans = "Pass";
		try
		{
			CompressedImageInterface img10_1 = new LinkedListImage("test_10_1.txt");
			boolean[][] img1 = new boolean[10][10];
			boolean[][] img2 = new boolean[10][10];
			fillGridFromFile(img1, "test_10_1.txt");
			fillGridFromFile(img2, "test_10_2.txt");
			CompressedImageInterface img10_2 = new LinkedListImage(img2, 10, 10);
			// for (int i = 0; i < 10; i++)
			// 	for (int j = 0; j < 10; j++)
			// 	{
			// 		System.out.print(img1[i][j] +" ");
			// 		if (j == 9) System.out.print("\n");
			// 	}
			img10_1.performAnd(img10_2);
			for (int i = 0; i < 10; i++)
				for (int j = 0; j < 10; j++)
					img1[i][j] = img1[i][j] && img2[i][j];
			String ans1 = gridToString(img1);
			// System.out.println("ans1 : " + ans1 + ", mank : " + img10_1.toStringCompressed());
			pass = pass && ans1.equals(img10_1.toStringCompressed());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e);
			pass = false;
		}
		if (pass)
			numPassed++;
		else
			ans = "Fail";
		printToFile(pw, numTest, ans, "performAnd for test_10_1, test_10_2");
	}

	public static void performOrTest(PrintWriter pw)
	{
		numTest++;
		boolean pass = true;
		String ans = "Pass";
		try
		{
			CompressedImageInterface img10_1 = new LinkedListImage("test_10_1.txt");
			CompressedImageInterface img10_2 = new LinkedListImage("test_10_2.txt");
			boolean[][] img1 = new boolean[10][10];
			boolean[][] img2 = new boolean[10][10];
			fillGridFromFile(img1, "test_10_1.txt");
			fillGridFromFile(img2, "test_10_2.txt");
			// for (int i = 0; i < 10; i++)
			// 	for (int j = 0; j < 10; j++)
			// 	{
			// 		System.out.print(img1[i][j] +" ");
			// 		if (j == 9) System.out.print("\n");
			// 	}
			img10_2.performOr(img10_1);
			for (int i = 0; i < 10; i++)
				for (int j = 0; j < 10; j++)
					img2[i][j] = img1[i][j] || img2[i][j];
			String ans2 = gridToString(img2);
			// System.out.println("ans1 : " + ans2 + ", mank : " + img10_2.toStringCompressed());
			pass = pass && ans2.equals(img10_2.toStringCompressed());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e);
			pass = false;
		}
		if (pass)
			numPassed++;
		else
			ans = "Fail";
		printToFile(pw, numTest, ans, "performOr for test_10_1, test_10_2");
	}

	public static void performXorTest(PrintWriter pw)
	{
		numTest++;
		boolean pass = true;
		String ans = "Pass";
		try
		{
			CompressedImageInterface img10_1 = new LinkedListImage("test_10_1.txt");
			CompressedImageInterface img10_2 = new LinkedListImage("test_10_2.txt");
			boolean[][] img1 = new boolean[10][10];
			boolean[][] img2 = new boolean[10][10];
			fillGridFromFile(img1, "test_10_1.txt");
			fillGridFromFile(img2, "test_10_2.txt");
			img10_2.performXor(img10_1);
			for (int i = 0; i < 10; i++)
				for (int j = 0; j < 10; j++)
					img2[i][j] = img1[i][j] ^ img2[i][j];
			String ans2 = gridToString(img2);
			pass = pass && ans2.equals(img10_2.toStringCompressed());

			// testing complexity :
			int sz = 599;
			CompressedImageInterface img1000_1 = new LinkedListImage("test_" + sz + "_1.txt");
			CompressedImageInterface img1000_2 = new LinkedListImage("test_" + sz + "_2.txt");
			img1 = new boolean[sz][sz];
			img2 = new boolean[sz][sz];
			fillGridFromFile(img1, "test_" + sz + "_1.txt");
			fillGridFromFile(img2, "test_" + sz + "_2.txt");
			img1000_1.performXor(img1000_2);
			for (int i = 0; i < sz; i++)
				for (int j = 0; j < sz; j++)
					img1[i][j] = img1[i][j] ^ img2[i][j];
			String ans1 = gridToString(img1);
			pass = pass && ans1.equals(img1000_1.toStringCompressed());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e);
			pass = false;
		}
		if (pass)
			numPassed++;
		else
			ans = "Fail";
		printToFile(pw, numTest, ans, "performXor for test_10_1, test_10_2, test_599_1, test_599_2");		
	}
}