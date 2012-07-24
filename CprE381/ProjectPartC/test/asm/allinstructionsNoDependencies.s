Begin:
	addi $t1, $zero, 9	#$9 = 9, 1001
	nop 
	nop
	nop
	andi $t2, $t1, 8		#$10 = 8, 1000
	nop 
	nop
	sh $t1, -1($t1)		#address at 9 = 1001
	nop 
	nop
	nop
	sw $t2, 3($t1)		#address at 10 = 1000
	nop 
	nop
	nop
	add $t3, $t1, $t2	#$11 = 17, 10001
	nop 
	nop
	nop
	addiu $t4, $t0, 32	#$12 = 32, 100000
	nop 
	nop
	nop
	addu $t5, $t4, $t3	#$13 = 49, 110001
	nop 
	nop
	nop
	and $t6, $t5, $t1	#$14 = 1, 0001
	nop 
	nop
	nop
	sub $t7, $t5, $t6	#$15 = 48, 110000
	nop 
	nop
	nop
	subu $t8, $t7, $t6	#$24 = 47, 101111
	nop 
	nop
	nop
	sll $t9, $t2, 1		#$25 = 16, 10000
	nop 
	nop
	nop
	sllv $t1, $t1, $t1	#$9 = 1 0010 0000 0000
	nop 
	nop
	nop
	or $t9, $t1, $t6		#$25 = 0001 0010 0000 0001
	nop 
	nop
	nop
	ori $t8, $t9, 15		#$24 = 0001 0010 0000 1111
	nop 
	nop
	nop
	xori $t8, $t9, 15	#$24 = 0001 0010 0000 1110
	nop 
	nop
	nop
	xor $t7, $t8, $t1	#$15 = 0000 0000 0000 1110
	nop 
	nop
	nop
	j BackToCode
	nop 
	nop
	nop
	
Next:
	jal NextTime
	nop 
	nop
	nop
	jr $ra
	nop 
	nop
	nop
	
BackToCode:
	lui $t4, 55555		#$12 = 1101 1001 0000 0011 0000 0000 0000 0000
	nop 
	nop
	nop
	srl $t3, $t4, 16	        #$11 = 0000 0000 0000 0000 1101 1001 0000 0011
	nop 
	nop
	nop
	sra $t2, $t4, 16   	#$10 = 1111 1111 1111 1111 1101 1001 0000 0011
	nop 
	nop
	nop
	lw $t5, 12($0)		#$13 = 1000
	nop 
	nop
	nop
	srlv $t3, $t4, $t5	#$11 = 0000 0000 1101 1001 0000 0011 0000 0000
	nop 
	nop
	nop
	srav $t2, $t4, $t5	#$10 = 1111 1111 1101 1001 0000 0011 0000 0000
	nop 
	nop
	nop
	sb $t5, 100($0)		#address of 100 = 1000
	nop 
	nop
	nop
	lh $t6, 8($0)		#$14 = 1001
	nop 
	nop
	nop
	lhu $t7, 8($0)		#$15 = 1001
	nop 
	nop
	nop
	mul $t8, $t6, $t7	#$24 = 1010001
	nop 
	nop
	nop
	nor $t8, $t6, $t7	#$24 = ffff fffff ffff 0110
	nop 
	nop
	nop
	lb $t9, 100($0)		#$25 = 1000
	nop 
	nop
	nop
	lbu $t8, 100($0)	#$24 = 1000
	nop 
	nop
	nop
	slt $t0, $t7, $t8		#$8 = 0
	nop 
	nop
	nop
	slt $t0, $t8, $t7	#$8 = 1
	nop 
	nop
	nop
	sltu $t0, $t7, $t8	#$8 = 0
	nop 
	nop
	nop
	sltu $t0, $t8, $t7	#$8 = 1
	nop 
	nop
	nop
	slti $t0, $t8, 5	#$8 = 0
	nop 
	nop
	nop
	slti $t0, $t8, 91	#$8 = 1
	nop 
	nop
	nop
	sltiu $t0, $t8, 5	#$8 = 0
	nop 
	nop
	nop
	sltiu $t0, $t8, 91	#$8 = 1
	nop 
	nop
	nop
AllBranches:
	j Next
	nop 
	nop
	nop
NextTime:
	jalr $ra
	nop 
	nop
	nop
	addi $t0, $zero, 0	#$8 = 0
	nop 
	nop
	nop
	beq $zero, $t0, notcool
	nop 
	nop
	nop
	addi $t0, $0, 1		#$8 = 1
	nop 
	nop
	nop
awesome:	
	bgez $t5, kindacool
	nop 
	nop
	nop
	addi $t0, $0, 181212
	nop 
	nop
	nop
veryhot:	
	bgezal $t5, lukewarm
	nop 
	nop
	nop
	addi $t0, $0, 181212
	nop 
	nop
	nop
kindahot:	
	bgtz $t5, freezing
	nop 
	nop
	nop
	addi $t0, $0, 181212
	nop 
	nop
	nop
scoulding:
	j verycool
	nop 
	nop
	nop
	addi $t0, $0, 181212
	nop 
	nop
	nop
notcool:
	addi $t0, $0, 2		#$8 = 1
	nop 
	nop
	nop
	bne $t0, $zero, awesome
	nop 
	nop
	nop
	addi $t0, $0, 181212
	nop 
	nop
	nop
kindacool:
	blez $t2, veryhot
	nop 
	nop
	nop
	addi $t0, $0, 181212
	nop 
	nop
	nop
lukewarm:
	bltz $t2, kindahot
	nop 
	nop
	nop
	addi $t0, $0, 181212
	nop 
	nop
	nop
freezing:
	bltzal $t2, verycool
	nop 
	nop
	nop
	addi $t0, $0, 181212
	nop 
	nop
	nop
verycool:
	

