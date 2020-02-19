# Name: Riti Mittal and Miriam Lam
# Section: 315-05
# Description: Take in two numbers (x,y) and returns the exponentiation (the result after raising x^y)

# Java code
# public static void main(String[] args)
# {
#     Scanner scanner = new Scanner(System.in);
#     System.out.println("Enter a number: ");
#     int num = scanner.nextInt();
#     System.out.println("Enter an exponent: ");
#     int exp = scanner.nextInt();
#     int result = num;
#     int partial = num;
#     int x = 1;
#     if (exp == 0)
#     {
#        System.out.println(1);
#        return;
#     }
#     while (exp > x)
#     {
#        for (int i = 1; i < num; i++)
#        {
#           result += partial;
#        }
#        partial = result;
#        x++;
#     }
# System.out.println(result);
# }

# declare global so programmer can see actual addresses.
.globl prompt1
.globl prompt2
.globl result

#  Data Area (this area contains strings to be displayed during the program)
.data

prompt1:
	.asciiz "Enter a number: \n"

prompt2:
	.asciiz "Enter an exponent: \n"

result:
	.asciiz "Result = "

#Text Area (i.e. instructions)
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

   # Display prompt2 (4 is loaded into $v0 to display)
   # 0x12 is hexidecimal for 18 decimal (the length of the previous prompt)
   ori     $v0, $0, 4
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x12
   syscall

   #Read the exponent from user input
   ori     $v0, $0, 5
   syscall
   # $v0 now has the value of the exponent
   add     $t5, $0, $v0

   add     $t2, $t0, $0 #result = num
   add     $t3, $t0, $0 # partial = num
   addi    $t4, $0, 1 # x=1

   beq     $t5, $0, base_case

loop:
   slt     $t6, $t4, $t5 #x < exp
   beq     $t6, $0, exit
   addi    $t1, $0, 1 #setting i = 1

subloop:
   slt     $t7, $t1, $t0 #i < num
   beq     $t7, $0, continue
   add     $t2, $t2, $t3 #result  = result + partial
   addi    $t1, $t1, 1
   j       subloop

continue:
   add     $t3, $0, $t2 #partial = result
   addi    $t4, $t4, 1 #x++
   j       loop

base_case:
   add     $t2, $0, 1 #if exponent is 0 then return 1

exit:

   #Display the result text
   ori     $v0, $0, 4
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x27
   syscall

   # Display the result
   # load 1 into $v0 to display an integer
   add     $a0, $0, $t2
   ori     $v0, $0, 1
   syscall

   # Exit (load 10 into $v0)
   ori     $v0, $0, 10
   syscall

