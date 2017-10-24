import random
sz = 100

def writeArrToFile(fname, arr):
	write_file = open(fname, 'w')
	s = len(arr)
	write_file.write(str(s) + ' ' + str(s) + '\n')
	for i in xrange(0, s):
		for j in xrange(0, s):
			write_file.write(str(arr[i][j]) + ' ')
		write_file.write('\n')

def transposeMat(arr):
	s = len(arr)
	new_arr = [[arr[i][j] for i in xrange(0, s)] for j in xrange(0, s)]
	return new_arr

arr = [[0 for i in xrange(0, sz)] for j in xrange(0, sz)]
# writeArrToFile('test_100_1.txt', arr)

# arr = [[1 for i in xrange(0, sz)] for j in xrange(0, sz)]
# writeArrToFile('test_100_2.txt', arr)

sz = 10

arr = [[0 for i in xrange(0, sz)] for j in xrange(0, sz)]

writeArrToFile('test_10_3.txt', arr)

arr = [[1 for i in xrange(0, sz)] for j in xrange(0, sz)]

writeArrToFile('test_10_4.txt', arr)

for i in xrange(0, sz):
	if i <= sz/2:
		for j in xrange(i, i+sz/2):
			arr[i][j] = 1
	else:
		for j in xrange(0, sz):
			arr[i][j] = i%2

# writeArrToFile('test_10_1.txt', arr)
# writeArrToFile('test_10_2.txt', transposeMat(arr))

sz = 599
arr = [[0 for i in xrange(0, sz)] for j in xrange(0, sz)]
for i in xrange(0, sz):
	arr[i][i] = 1

writeArrToFile('test_599_1.txt', arr)

arr = [[0 for i in xrange(0, sz)] for j in xrange(0, sz)]
for i in xrange(0, sz):
	arr[i][sz - i - 1] = 1

writeArrToFile('test_599_2.txt', arr)

sz = 100
arr = [[0 for i in xrange(0, sz)] for j in xrange(0, sz)]
for i in xrange(0, sz):
	for j in xrange(0, sz):
		arr[i][j] = random.randint(0,1)

# writeArrToFile('test_100_3.txt', arr)