Begin:
	addi $t1, $zero, 9	#$9 = 9, 1001
	andi $t2, $t1, 8	#$10 = 8, 1000
	sh $t1, -1($t1)		#address at 9 = 1001
	sw $t2, 3($t1)		#address at 10 = 1000
	add $t3, $t1, $t2	#$11 = 17, 10001
	addiu $t4, $t0, 32	#$12 = 32, 100000
	addu $t5, $t4, $t3	#$13 = 49, 110001
	and $t6, $t5, $t1	#$14 = 1, 0001
	sub $t7, $t5, $t6	#$15 = 48, 110000
	subu $t8, $t7, $t6	#$24 = 47, 101111
	sll $t9, $t2, 1	#$25 = 16, 10000
	sllv $t1, $t1, $t1	#$9 = 1 0010 0000 0000
	or $t9, $t1, $t6	#$25 = 0001 0010 0000 0001
	ori $t8, $t9, 15	#$24 = 0001 0010 0000 1111
	xori $t8, $t9, 15	#$24 = 0001 0010 0000 1110
	xor $t7, $t8, $t1	#$15 = 0000 0000 0000 1110
	j BackToCode
	
Next:
	jal NextTime
	jr $ra
	
BackToCode:
	lui $t4, 55555		#$12 = 1101 1001 0000 0011 0000 0000 0000 0000
	srl $t3, $t4, 16	        #$11 = 0000 0000 0000 0000 1101 1001 0000 0011
	sra $t2, $t4, 16   	#$10 = 1111 1111 1111 1111 1101 1001 0000 0011
	lw $t5, 12($0)		#$13 = 1000
	srlv $t3, $t4, $t5	#$11 = 0000 0000 1101 1001 0000 0011 0000 0000
	srav $t2, $t4, $t5	#$10 = 1111 1111 1101 1001 0000 0011 0000 0000
	sb $t5, 100($0)		#address of 100 = 1000
	lh $t6, 8($0)		#$14 = 1001
	lhu $t7, 8($0)		#$15 = 1001
	mul $t8, $t6, $t7	#$24 = 1010001
	nor $t8, $t6, $t7	#$24 = ffff fffff ffff 0110
	lb $t9, 100($0)		#$25 = 1000
	lbu $t8, 100($0)	#$24 = 1000
	slt $t0, $t7, $t8		#$8 = 0
	slt $t0, $t8, $t7	#$8 = 1
	sltu $t0, $t7, $t8	#$8 = 0
	sltu $t0, $t8, $t7	#$8 = 1
	slti $t0, $t8, 5	#$8 = 0
	slti $t0, $t8, 91	#$8 = 1
	sltiu $t0, $t8, 5	#$8 = 0
	sltiu $t0, $t8, 91	#$8 = 1
AllBranches:
	j Next
NextTime:
	jalr $ra
	addi $t0, $zero, 0	#$8 = 0
	beq $zero, $t0, notcool
	addi $t0, $0, 1		#$8 = 1
awesome:	
	bgez $t5, kindacool
	addi $t0, $0, 181212
veryhot:	
	bgezal $t5, lukewarm
	addi $t0, $0, 181212
kindahot:	
	bgtz $t5, freezing
	addi $t0, $0, 181212
scoulding:
	j verycool
	addi $t0, $0, 181212
notcool:
	addi $t0, $0, 2		#$8 = 1
	bne $t0, $zero, awesome
	addi $t0, $0, 181212
kindacool:
	blez $t2, veryhot
	addi $t0, $0, 181212
lukewarm:
	bltz $t2, kindahot
	addi $t0, $0, 181212
freezing:
	bltzal $t2, verycool
	addi $t0, $0, 181212
verycool:
	

