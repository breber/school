# Group 9 
# MergeSort.s
# 
# MIPS Code that performs the Merge Sort Algorithm
add $s0, $zero, 4	# $s0 holds the starting address of the array
lw $s1, 0($zero)	# $s1 holds the number of elements in the array

sll $s2, $s1, 2		# convert total number elements to bytes

add $a0, $zero, $zero	# initialize the 'left' parameter for our initial entry into merge_sort
add $a1, $zero, $s1	# initialize the 'right' parameter for our initial entry into merge_sort

sub $sp, $sp, $s2	# Create enough room on the stack for our temp array
addi $sp, $sp, -4	# Add an extra element just in case
add $s3, $sp, $zero	# $s3 = &temp

Main:
	jal MergeSort	# Start Processing
	j Exit		# We are done - go to Exit

# Merge Sort Algorithm


# $a0 = left
# $a1 = right
MergeSort:
	sub $t0, $a1, $a0		# $t0 = right - left
	slti $t1, $t0, 2		# $t1 = $t0 < 2
	beqz $t1, SetUpRecursion	# if (right - left not < 2), go to SetUpRecursion
	jr $ra                  # right - left is < 2, so return

	SetUpRecursion:
	addi $sp, $sp, -16		# make space for 3 registers on the stack
	sw $ra, 0($sp)			# store the return address onto the stack
	sw $a0, -4($sp)			# store current 'left' value onto the stack
	sw $a1, -8($sp)			# store current 'right' value onto the stack
	
	srl $t1, $t0, 1			# $t1 = (right - left) / 2
	add $a1, $a0, $t1		# $a1 = left + mid
	sw $a1, -12($sp)		# store current 'mid' onto the stack
	jal MergeSort			# mergesort(start, mid)
	
	lw $a0, -12($sp)		# Grab 'mid' off the stack
	lw $a1, -8($sp)			# Grab 'right' off the stack
	jal MergeSort			# mergesort(mid, right)
	
	lw $ra, 0($sp)			# Restore the return address
	
Merge:
	# Register Usage
	# $t0 = left
	# $t1 = right
	# $t2 = mid
	# $t3 = current_pos
	# $t4 = left_i
	# $t5 = right_i
	# $t6 = Memory address calculation
	# $t7 = Memory address calculation
	# $t8 = Memory Data
	# $t9 = Memory Data
	
	lw $t0, -4($sp)			# Grab 'left' off the stack (element number)
	lw $t1, -8($sp)			# Grab 'right' off the stack (element number)
	
	sub $t2, $t1, $t0		# $t2 = right - left
	srl $t2, $t2, 1			# $t1 = (right - left) / 2
	add $t2, $t0, $t2		# $t2 = left + mid
	
	sll $t0, $t0, 2			# Convert 'left' to bytes
	sll $t1, $t1, 2			# Convert 'right' to bytes
	sll $t2, $t2, 2			# Convert 'mid' to bytes
	
	add $t3, $zero, $t0		# current_pos = start
	add $t4, $zero, $t0		# left_i = start
	add $t5, $zero, $t2		# right_i = mid
	
Loop1:
	slt $t8, $t4, $t2		# $t8 = left_i < mid
	slt $t9, $t5, $t1		# $t9 = right_i < right
	or $t8, $t8, $t9		# left_i < mid || right_i < right
	beqz $t8, Loop2			# if above statement is false, go to Loop2
	
	# We are now in the loop...
	Cond1:
		bne $t5, $t1, Cond2		# Skip following statement if right_i != right
		add $t6, $s0, $t4	# $t6 = &list[left_i]
		lw $t8, 0($t6)		# $t8 = list[left_i]
		addi $t4, $t4, 4	# left_i++
	
		add $t7, $s3, $t3	# $t7 = &temp[current_pos]
		sw $t8, 0($t7)		# temp[current_pos] = list[left_i]
		addi $t3, $t3, 4	# current_pos++
		j Loop1			# Go back to beginning of loop
	Cond2:
		bne $t4, $t2, Cond3	# Skip following statement if left_i != mid
		add $t6, $s0, $t5	# $t6 = &list[right_i]
		lw $t8, 0($t6)		# $t8 = list[right_i]
		addi $t5, $t5, 4	# right_i++
	
		add $t7, $s3, $t3	# $t7 = &temp[current_pos]
		sw $t8, 0($t7)		# temp[current_pos] = list[right_i]
		addi $t3, $t3, 4	# current_pos++
		j Loop1			# Go back to beginning of loop
	Cond3:
		add $t6, $s0, $t4	# $t6 = &list[left_i]
		lw $t8, 0($t6)		# $t8 = list[left_i]
		add $t7, $s0, $t5	# $t7 = &list[right_i]
		lw $t9, 0($t7)		# $t9 = list[right_i]
		
		bge $t8, $t9, Cond4	# Skip following statement if list[left_i] >= list[right_i]

		# list[left_i] is already in $t8
		addi $t4, $t4, 4	# left_i++
	
		add $t7, $s3, $t3	# $t7 = &temp[current_pos]
		sw $t8, 0($t7)		# temp[current_pos] = list[left_i]
		addi $t3, $t3, 4	# current_pos++
		j Loop1			# Go back to beginning of loop
	Cond4:
		add $t7, $s0, $t5	# $t7 = &list[right_i]
		lw $t9, 0($t7)		# $t9 = list[right_i]
		addi $t5, $t5, 4	# right_i++
	
		add $t7, $s3, $t3	# $t7 = &temp[current_pos]
		sw $t9, 0($t7)		# temp[current_pos] = list[left_i]
		addi $t3, $t3, 4	# current_pos++
		j Loop1			# Go back to beginning of loop
Loop2:
	add $t3, $zero, $t0	# current_pos = start
Loop2Loop:
	bge $t3, $t1, CleanUp	# if current_pos >= end, go to CleanUp
	add $t6, $s3, $t3	# $t6 = &temp[current_pos]
	lw $t8, 0($t6)		# $t8 = temp[current_pos]
	
	add $t7, $s0, $t3	# $t7 = &list[current_pos]
	sw $t8, 0($t7)		# list[current_pos] = temp[current_pos]
	addi $t3, $t3, 4	# current_pos++
	j Loop2Loop		# Go back to beginning of this loop
CleanUp:
	addi $sp, $sp, 16	# restore the stack
	jr $ra			# return to caller
Exit:
