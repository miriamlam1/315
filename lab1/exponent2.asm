# Name: Riti Mittal and Miriam Lam
# Section: 315-05

# declare global so programmer can see actual addresses.
.globl prompt1
.globl prompt2
.globl result

#  Data Area (this area contains strings to be displayed during the program)
.data

prompt1:
   .asciiz "Enter a number: \n"

prompt2:
   .asciiz "Enter a exponent: \n"

result:
   .asciiz "Exponent is: "

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

   #Read the exponent from user inpit
   ori     $v0, $0, 5
   syscall
   # $v0 now has the value of the exponent
   
   #sum = num
   add $t4, $t0, $0

loop:
   add $t4, $t0, $0
   beq $v0, $0, base_case #base
   beq $v0, 1, end        #if exp =1

   add $t1, $t4, $0 #init temp(t1) to sum
   add $t4, $0, $0  #sum =0

   add $t2, $t0, $0 #init count to num

f_loop:
   add $t4, $t4, $t1 #sum+=tmep
   addi $t2, $t2, -1 #inc count
   bne $t2,$0, f_loop 

   addi $v0, $v0, -1 #decrement exp 
   j loop

#end
end:
   #Display the result text
   ori     $v0, $0, 4
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x26
   syscall 
 
   # Display the result
   # load 1 into $v0 to display an integer
   add     $a0, $0, $t4
   ori     $v0, $0, 1
   syscall

   # Exit (load 10 into $v0)
   ori     $v0, $0, 10
   syscall

base_case:
   addi $t4, $0, 1
   j end
   
   
