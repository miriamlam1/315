# Name: Riti Mittal and Miriam Lam
# Section: 315-05
# Description:

# 3. Write a function which divides a 64-bit unsigned number with a 31-bit unsigned number.
# The 31-bit divisor is guaranteed to be a power of 2.  The program should take in the 64-bit
# as 2 32-bit numbers: first the upper 32 bits, then the lower 32 bits.
# The answer should be printed out as 2 32-bit numbers.  Do not worry if the output of your
# program is a negative number.  PCSPIM prints out numbers as signed numbers, so if one of the
# answers happens to have a leading 1, the number will appear as negative.  Here are some test
# cases (shown as dividend high 32, dividend low 32 / divisor = quotient high 32,
# quotient low 32): 1,1 / 65536 = 0,65536 and 2,10 / 65536 = 0,131072 and 42,32 / 32 =
# 11342177281 and 210,64 / 64 = 3, 1207959553. Name your file divide.asm. Program 3
# only needs to work with positive numbers.

# Java code

# declare global so programmer can see actual addresses.
.globl prompt1
.globl prompt2
.globl prompt3
.globl result

#  Data Area (this area contains strings to be displayed during the program)
.data

prompt1:
	.asciiz "Enter upper 32 bits: \n"

prompt2:
	.asciiz "Enter lower 32 bits: \n"

prompt3:
	.asciiz "Enter divisor: \n"

result:
	.asciiz "Result = "

comma:
   .asciiz ","

.text

main:
   # Display prompt1 (load 4 into $v0 to display)
   ori     $v0, $0, 4
   lui     $a0, 0x1001
   syscall

   # Reads the number from user input
   ori     $v0, $0, 5
   syscall

   #Add the number into $t0
   add     $t0, $0, $v0 #num is in $t0



