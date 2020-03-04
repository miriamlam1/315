# Name: Riti Mittal and Miriam Lam
# Section: 315-05
# Description: This program takes in an input number as two 32 bit numbers (to represent a 64 bit number) as well as a divisor.
# It divides the 64 bit number by the divisor and returns the result as two 32 bit numbers.

# Java code
# public static void main(String []args)
# {
#    Scanner scanner = new Scanner(System.in);
#    System.out.println("Enter upper: ");
#    int up = scanner.nextInt();
#    System.out.println("Enter lower: ");
#    int low = scanner.nextInt();
#    System.out.println("Enter divisor: ");
#    int div = scanner.nextInt();
#    int temp = 1;
#    int num = 0;
#    while(temp<div)
#    {
#       temp <<= 1;
#       low >>= 1;
#       num = up & 1;
#       num = (int)num;
#       num <<= 31;
#       low = low + num;
#       up >>= 1;
#    }
#    System.out.printf("%d, %d", up, low);
# }

# declare global so programmer can see actual addresses.
.globl prompt1
.globl prompt2
.globl prompt3
.globl result
.globl comma

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

	# This generates the starting address for prompt1
	# (assumes the register first contains 0).
	lui     $a0, 0x1001
	syscall

	# Reads the number from user input
	ori     $v0, $0, 5
   syscall

   #Add the number into $t0
   add     $t0, $0, $v0

   # Display prompt2 (4 is loaded into $v0 to display)
   # 0x12 is hexidecimal for 18 decimal (the length of the previous prompt)

   ori     $v0, $0, 4
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x17
   syscall

   #Read the divisor from user input
   ori     $v0, $0, 5
   syscall

   add     $t1, $0, $v0 # $t1 = lower 32 bits

   ori     $v0, $0, 4
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x2E
   syscall

   ori     $v0, $0, 5 #v0 = divisor
   syscall

   addi    $t2, $0, 1 #t2 = temp = 1
   addi    $t3, $0, 0 #t3 = num = 0

loop:
   slt     $t4, $t2, $v0 #temp < div
   beq     $t4, $0, exit
   sll     $t2, $t2, 1 #temp <<= 1
   srl     $t1, $t1, 1 #low >>= 1
   andi    $t3, $t0, 1
   sll     $t3, $t3, 31 #num <<= 31;
   add     $t1, $t1, $t3 #low = low + num;
   srl     $t0, $t0, 1 #up >>= 1
   j       loop

exit:
   #Prints the result text
   ori     $v0, $0, 4
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x3F
   syscall

   #Prints the upper quotient
   add     $a0, $0, $t0
   ori     $v0, $0, 1
   syscall

   #Load comma
   ori     $v0, $0, 4
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x49
   syscall

   #Prints the lower quotient
   add     $a0, $0, $t1
   ori     $v0, $0, 1
   syscall

   # Exit (load 10 into $v0)
   ori     $v0, $0, 10
   syscall





















