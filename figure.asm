# http://users.csc.calpoly.edu/~jseng/Winter20/lab5/Lab5.html

main:
    addi $s0, $0, 0 #datamem counter 5
    # Circle(30,100,20) #head
    addi $a0, $0, 30 #xc
    addi $a1, $0, 100 #yc
    addi $a2, $0, 20 #r 
    jal circle
    # Line(30,80,30,30) #body
    addi $a0, $0, 30 #x0 
    addi $a1, $0, 80 #y0 
    addi $a2, $0, 30 #x1 
    addi $a3, $0, 30 #y1 
    jal line
    # Line(20,1,30,30) #left leg
    addi $a0, $0, 20 #x0 
    addi $a1, $0, 1 #y0 
    addi $a2, $0, 30 #x1 
    addi $a3, $0, 30 #y1 
    jal line
    # Line(40,1,30,30) #right leg
    addi $a0, $0, 40 #x0 
    addi $a1, $0, 1 #y0 
    addi $a2, $0, 30 #x1 
    addi $a3, $0, 30 #y1 
    jal line
    # Line(15,60,30,50) #left arm
    addi $a0, $0, 15 #x0 
    addi $a1, $0, 60 #y0 
    addi $a2, $0, 30 #x1 
    addi $a3, $0, 50 #y1 
    jal line
    # Line(30,50,45,60) #right arm
    addi $a0, $0, 30 #x0 
    addi $a1, $0, 50 #y0 
    addi $a2, $0, 45 #x1 
    addi $a3, $0, 60 #y1 
    jal line
    # Circle(24,105,3) #left eye
    addi $a0, $0, 24 #xc
    addi $a1, $0, 105 #yc
    addi $a2, $0, 3 #r 
    jal circle
    # Circle(36,105,3) #right eye
    addi $a0, $0, 36 #xc
    addi $a1, $0, 105 #yc
    addi $a2, $0, 3 #r 
    jal circle
    # Line(25,90,35,90) #mouth center
    addi $a0, $0, 25 #x0 
    addi $a1, $0, 90 #y0 
    addi $a2, $0, 35 #x1 
    addi $a3, $0, 90 #y1 
    jal line
    # Line(25,90,20,95) #mouth left
    addi $a0, $0, 25 #x0 
    addi $a1, $0, 90 #y0 
    addi $a2, $0, 20 #x1 
    addi $a3, $0, 95 #y1 
    jal line
    # Line(35,90,40,95) #mouth right
    addi $a0, $0, 35 #x0 
    addi $a1, $0, 90 #y0 
    addi $a2, $0, 40 #x1 
    addi $a3, $0, 95 #y1 
    jal line
    j end

line:
#absolute values
    #t0 = 
    sub $t0, $a3, $a1 #t0 = y1-y0 7
    slt $t2, $t0, $0 #if (y1-y0) is less than 0, t2 =1 8
    beq $t2, $0, skip1 #if positive already skip 9
    sub $t0, $0, $t0 # -(y1-y0) 10
skip1:
    #t4 = 
    sub $t1, $a2, $a0 #t1 = x1-x0 11
    add $t4, $0, $t1 #12
    slt $t2, $t0, $0  #13
    beq $t2, $0, skip2 #14
    sub $t4, $0, $t1 #t4 = abs(x1-x0) 15
skip2:
# set state = t2
    slt $t2, $t4, $t0 #if abs(y1 - y0) > abs(x1 - x0), state =1

#swap
beq $t2, $0, skip3 #if state == 0 skip
    #swap x0y0
    add $t3, $0, $a0 #temp = x0
    add $a0, $0, $a1 #x0 = x1
    add $a1, $0, $t3 #x1 = temp
    #swap x1,y1
    add $t3, $0, $a2 #temp = y0
    add $a2, $0, $a3 #y0 = y1
    add $a3, $0, $t3 #y1 = temp

skip3:

slt $t3, $a2, $a0
beq $t3, $0, skip43
#swap x0 x1
    add $t3, $0, $a0 #temp = x0
    add $a0, $0, $a2 #x0 = x1
    add $a2, $0, $t3 #x1 = temp
#swap y0 y1
    add $t3, $0, $a1 #temp = y0
    add $a1, $0, $a3 #y0 = y1
    add $a3, $0, $t3 #y1 = temp

skip43:
    #deltax = t1
    #deltay = t0
    addi $t7, $0, 0 #error = t7
    #y = y0 a1

    slt $t3, $a3, $a1 #if y0<y1
    bne $t3, $0, skip4 
    addi $t5, $0, 1 #ystep = 1
    j loop
skip4:
    addi $t5, $0, -1 #ystep = -1

#lt $t6, $a0, $a2 #t6 = 1 when x0 < x1

loop:
    #slt $t3, $a0, $0
    beq $t2, $0, plotxy
plotyx:
    sw $a1, 0($s0)
    addi $s0, $s0, 1
    sw $a0, 0($s0)
    addi $s0, $s0, 1
    j skip6
plotxy:
    sw $a0, 0($s0)
    addi $s0, $s0, 1
    sw $a1, 0($s0)
    addi $s0, $s0, 1

skip6:
    add $t7, $t7, $t0 #error = error +deltay
    sll $t4, $t7, 1 #error*2

    addi $t4, $t4, 1
    slt $t3, $t4, $t1
    bne $t3, $0, skip

    add $a1, $a1, $t5
    sub $t7, $t7, $t1

skip:
    beq $a0, $a2, endline

    #ijfiljsdfksdf
#    beq $t6, $0, skip9
    addi $a0, $a0, 1
 #   j skip8

#skip9:
#    addi $a0, $a0, -1

skip8: 
    j loop

endline:
    jr $ra

end:


    
    
















    