run: compile test

compile:
	javac *.java

test:
	java CompressedImageChecker

clean:
	-rm *.class
	mv CompressedImageInterface.java CompressedImageInterface.jav
	mv CompressedImageChecker.java CompressedImageChecker.jav
	-rm ImageTestResult.txt
	-rm *.java
	mv CompressedImageInterface.jav CompressedImageInterface.java
	mv CompressedImageChecker.jav CompressedImageChecker.java