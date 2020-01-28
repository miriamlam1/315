# Name: Riti Mittal and Miriam Lam
# Section: 315-05
# Description: This program takes in a input number and returns the reverse-ordered binary of the input number in decimal.

# Java code
# public static void main(String[] args)
# {
#   Scanner scanner = new Scanner(System.in);
#   System.out.println("Enter a number: ");
#   int num = scanner.nextInt();
#   int rev = 0;

#   for (int i = 0; i < 31; i++)
#   {
#      int x = num & 1; //check if last bit is 1
#      num = num >> 1; //right shift the number
#      rev = rev | x;
#      rev = rev << 1; //left shift the result
#   }
#   System.out.println("Reversed number: " + rev);
# }

# declare global so programmer can see actual addresses.
.globl prompt1
.globl result

#  Data Area (this area contains strings to be displayed during the program)
.data

prompt1:
	.asciiz "Enter a number: \n"

result:
	.asciiz "Reverse-ordered binary of input number = "

#Text Area (i.e. instructions)
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

   add     $t1, $0, $0 #set rev = 0
   add     $t2, $0, $0 #set i=0

loop:
   andi    $t3, $v0, 1 #num & 1
   srl     $v0, $v0, 1 #right shift num
   or      $t1, $t1, $t3 #rev = rev | num
   sll     $t1, $t1, 1 #left shift rev
   addi    $t2, $t2, 1 #increment counter for loop

   slti    $t4, $t2, 31 # i < 31
   beq     $t4, $0, exit #if we reach 31 then exit
   j       loop #otherwise continue the loop

exit:
   #Display the result text
   ori     $v0, $0, 4
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x12
   syscall

   # Display the result
   # load 1 into $v0 to display an integer
   add     $a0, $0, $t1
   ori     $v0, $0, 1
   syscall

   # Exit (load 10 into $v0)
   ori     $v0, $0, 10
   syscall



