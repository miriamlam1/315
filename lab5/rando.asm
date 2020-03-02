# NOT OURS!! USED TO OCMPARE DATA
# https://github.com/dhruvsus/cpe315/blob/master/lab5/figure.asm
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
slt $t0, $a2, $a0
beq $t0, $0, dX
sub $t0, $a0, $a2
j dY
dX:
 sub $t0, $a2, $a0
dY:
 slt $t1, $a3, $a1
beq $t1, $0, nonABS
sub $t1, $a1, $a3
j abs
nonABS:
 sub $t1, $a3, $a1
abs: addi $t3, $0, 1 
slt $t2, $t0, $t1 
bne $t2, $0, else
add $t3, $0, $0
else: 
beq $t3, $0, swap
add $t4, $0, $a0 
add $a0, $0, $a1 
add $a1, $0, $t4 
add $t4, $0, $a2 
add $a2, $0, $a3 
add $a3, $0, $t4 
swap:
 slt $t4, $a2, $a0
beq $t4, $0, yCheck
add $t4, $0, $a0 
add $a0, $0, $a2 
add $a2, $0, $t4 
add $t4, $0, $a1 
add $a1, $0, $a3 
add $a3, $0, $t4 
yCheck: 
sub $t0, $a2, $a0
slt $t1, $a3, $a1
beq $t1, $0, negativeY
sub $t1, $a1, $a3
j err
negativeY: 
sub $t1, $a3, $a1
err: 
add $t2, $0, $0				
add $t4, $0, $a1
addi $t5, $0, 1 			
slt $t6, $a1, $a3
bne $t6, $0, startLoop
addi $t5, $0, -1
startLoop: 
add $t6, $0, $a0 
addi $t7, $a2, 1
lloop: 
beq $t6, $t7, endLine 
beq $t3, $0, plotXY
plotYX: 
sw $t4, 0($sp)
sw $t6, 1($sp)
addi $sp, $sp, 2
j computeErr
plotXY:
 sw $t6, 0($sp)
sw $t4, 1($sp)
addi $sp, $sp, 2
computeErr: 
add $t2, $t2, $t1 
sll $t8, $t2, 1 
slt $t9, $t8, $t0 
bne $t9, $0, inc 
add $t4, $t4, $t5
sub $t2, $t2, $t0
inc: addi $t6, $t6, 1
j lloop
endLine: 
jr $ra



circle: 
add $t0, $0, $0 
add $t1, $a2, $0 
sll $t6, $a2, 1 
addi $t8, $0, 3 
sub $t2, $t8, $t6 
sll $t4, $a2, 2 
addi $t8, $0, 10 
sub $t4, $t8, $t4 
addi $t5, $0, 6 
addi $t7, $t1, 1
circleStart:
 beq $t0, $t7, circleEnd 
add $t9, $a0, $t0 
add $s0, $a1, $t1 
sub $s2, $a1, $t1 
sub $s3, $a0, $t0 
add $s4, $a0, $t1 
add $s5, $a1, $t0 
sub $s6, $a0, $t1 
sub $t8, $a1, $t0 
sw $t9, 0($sp)
sw $s0, 1($sp)
addi $sp, $sp, 2
sw $t9, 0($sp)
sw $s2, 1($sp)
addi $sp, $sp, 2
sw $s3, 0($sp)
sw $s0, 1($sp)
addi $sp, $sp, 2
sw $s3, 0($sp)
sw $s2, 1($sp)
addi $sp, $sp, 2
sw $s4, 0($sp)
sw $s5, 1($sp)
addi $sp, $sp, 2
sw $s4, 0($sp)
sw $t8, 1($sp)
addi $sp, $sp, 2
sw $s6, 0($sp)
sw $s5, 1($sp)
addi $sp, $sp, 2
sw $s6, 0($sp)
sw $t8, 1($sp)
addi $sp, $sp, 2
slt $s1, $t2, $0 
bne $s1, $0, elseS 
ifS: 
add $t2, $t2, $t4 
addi $t4, $t4, 8 
addi $t7, $t1, 1
addi $t1, $t1, -1 
j  end_2
elseS: 
add $t2, $t2, $t5 
addi $t4, $t4, 4 
 end_2: 
 addi $t5, $t5, 4 
addi $t0, $t0, 1 
j circleStart
circleEnd: 
jr $ra
end: 
add $0, $0, $0 
