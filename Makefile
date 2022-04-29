lab2:
	javac Lab2/lab2.java

tests:
	java Lab2/lab2 Lab2/test1.asm > Lab2/out1
	diff -w -B Lab2/test1.output Lab2/out1
	java Lab2/lab2 Lab2/test2.asm > Lab2/out2
	diff -w -B Lab2/test2.output Lab2/out2
	java Lab2/lab2 Lab2/test3.asm > Lab2/out3
	diff -w -B Lab2/test3.output Lab2/out3
	java Lab2/lab2 Lab2/test4.asm > Lab2/out4
	diff -w -B Lab2/test4.output Lab2/out4

test1:
	java Lab2/lab2 Lab2/test1.asm > Lab2/out1
	diff -w -B Lab2/test1.output Lab2/out1

test2:
	java Lab2/lab2 Lab2/test2.asm > Lab2/out2
	diff -w -B Lab2/test2.output Lab2/out2

test3:
	java Lab2/lab2 Lab2/test3.asm > Lab2/out3
	diff -w -B Lab2/test3.output Lab2/out3

test4:
	java Lab2/lab2 Lab2/test4.asm > Lab2/out4
	diff -w -B Lab2/test4.output Lab2/out4
