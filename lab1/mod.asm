# Name: Riti Mittal and Miriam Lam
# Section: 315-05
# Description: Takes in two numbers and returns the remainder after division (returns the mod)


# Java code
#public static void main(String[] args)
#   {
#      Scanner scanner = new Scanner(System.in);
#      System.out.println("Enter a number: ");
#      int num = scanner.nextInt();
#      System.out.println("Enter a divisor: ");
#      int div = scanner.nextInt();
#      div--;
#      int result = num & div;
#      System.out.println(result);
#   }

# declare global so programmer can see actual addresses.
.globl prompt1
.globl prompt2
.globl result

#  Data Area (this area contains strings to be displayed during the program)
.data

prompt1:
   .asciiz "Enter a number: \n"

prompt2:
   .asciiz "Enter a divisor: \n"

result:
   .asciiz "Mod = "

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

   #Add the number into $t0
   add     $t0, $0, $v0
   
   # Display prompt2 (4 is loaded into $v0 to display)
   # 0x12 is hexidecimal for 18 decimal (the length of the previous prompt)

   ori     $v0, $0, 4
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x12
   syscall

   #Read the divisor from user inpit
   ori     $v0, $0, 5
   syscall
   # $v0 now has the value of the divisor

   # Subtract 1 from divisor and and it with the number
   add     $t1, $0, $v0
   addi    $t2, $t1, -1
   and     $t4, $t0, $t2
 
   #Display the result text
   ori     $v0, $0, 4
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x25
   syscall 
 
   # Display the result
   # load 1 into $v0 to display an integer
   add     $a0, $0, $t4
   ori     $v0, $0, 1
   syscall

   # Exit (load 10 into $v0)
   ori     $v0, $0, 10
   syscall
   
