lab2:
	javac Lab2/lab2.java

L2tests:
	java Lab2/lab2 Lab2/test1.asm > Lab2/out1
	diff -w -B Lab2/test1.output Lab2/out1
	java Lab2/lab2 Lab2/test2.asm > Lab2/out2
	diff -w -B Lab2/test2.output Lab2/out2
	java Lab2/lab2 Lab2/test3.asm > Lab2/out3
	diff -w -B Lab2/test3.output Lab2/out3
	java Lab2/lab2 Lab2/test4.asm > Lab2/out4
	diff -w -B Lab2/test4.output Lab2/out4

L2test1:
	java Lab2/lab2 Lab2/test1.asm > Lab2/out1
	diff -w -B Lab2/test1.output Lab2/out1

L2test2:
	java Lab2/lab2 Lab2/test2.asm > Lab2/out2
	diff -w -B Lab2/test2.output Lab2/out2

L2test3:
	java Lab2/lab2 Lab2/test3.asm > Lab2/out3
	diff -w -B Lab2/test3.output Lab2/out3

L2test4:
	java Lab2/lab2 Lab2/test4.asm > Lab2/out4
	diff -w -B Lab2/test4.output Lab2/out4

lab3:
	javac Lab3/lab3.java

L3test1:
	java Lab3/lab3 Lab3/test1.asm Lab3/script1 > Lab3/out1
	diff -w -B Lab3/output1 Lab3/out1

L3test2:
	java Lab3/lab3 Lab3/test2.asm Lab3/script2 > Lab3/out2
	diff -w -B Lab3/output2 Lab3/out2

L3test3:
	java Lab3/lab3 Lab3/test3.asm Lab3/script3 > Lab3/out3
	diff -w -B Lab3/output3 Lab3/out3

lab4:
	javac Lab4/lab4.java

L4test1:
	java Lab4/lab4 Lab4/test1.asm Lab4/script1 > Lab4/out1
	diff -w -B Lab4/output1 Lab4/out1

L4test2:
	java Lab4/lab4 Lab4/test2.asm Lab4/script2 > Lab4/out2
	diff -w -B Lab4/output2 Lab4/out2

L4test3:
	java Lab4/lab4 Lab4/test3.asm Lab4/script3 > Lab4/out3
	diff -w -B Lab4/output3 Lab4/output3

L4test4:
	java Lab4/lab4 Lab4/test4.asm Lab4/script4 > Lab4/out4
	diff -w -B Lab4/output4 Lab4/out4