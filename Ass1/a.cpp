#include <iostream>
using namespace std;

int main()
{
	cout << 10000001 << endl;
	for (int i = 0; i < 10000000; i++)
	{
		cout << i + 1 << " " << "1 ";
		cout << 10000000 - i << endl;
	}
	cout << 10000001 << " " << 2 << endl;
}