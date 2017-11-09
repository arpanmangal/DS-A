// An iterative C++ program to find all permutations of
// a given string.
#include<bits/stdc++.h>
using namespace std;

// A utility function to find n!
int fact(int n)
{
	return (n==1)? 1 : n*fact(n-1);
}

// An iterative function to print all permutations of s.
void printPermutation(string s)
{
	// Find length of string and factorial of length
	int n = s.length();
	int fc = fact(n);

	// Point j to the 2nd position
	int j = 1;

	// To store position of character to be fixed next.
	// m is used as in index in s[].
	int m = 0;

	// Iterate while permutation count is
	// smaller than n! which fc
	for (int perm_c = 0; perm_c < fc; )
	{
		// Store perm as current permutation
		string perm = s;

		// Fix the first position and iterate (n-1)
		// characters upto (n-1)!
		// k is number of iterations for current first
		// character.
		int k = 0;
		while (k != fc/n)
		{
			// Swap jth value till it reaches the end position
			while (j != n-1)
			{
				// Print current permutation
				cout << perm << "\n";

				// Swap perm[j] with next character
				swap(perm[j], perm[j+1]);

				// Increment count of permutations for this
				// cycle.
				k++;

				// Increment permutation count
				perm_c++;

				// Increment 'j' to swap with next character
				j++;
			}

			// Again point j to the 2nd position
			j = 1;
		}

		// Move to next character to be fixed in s[]
		m++;

		// If all characters have been placed at
		if (m == n)
		break;

		// Move next character to first position
		swap(s[0], s[m]);
		cout << endl;
	}
}

// Driver code
int main()
{
	string s = "012345";
	printPermutation(s);
	return 0;
}
