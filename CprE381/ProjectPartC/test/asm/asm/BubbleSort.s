# Group 9 
# BubbleSort.s
# 
# MIPS Code that performs the Bubble Sort Algorithm
addi $s0, $zero, 4	# $s0 holds the starting address of the array
lw $s1, 0($zero)	# $s1 holds the number of elements in the array

add $s2, $zero, $zero	# $s2 is our loop counter
addi $s3, $zero, 1		# $s3 is our swapped value

addi $s1, $s1, -1		# For our loop, we want to increment to (n - 1)
sll $s1, $s1, 2			# Convert (n - 1) to bytes


# Bubble Sort Algorithm
OuterLoop:
	beq $s3, $zero, Exit		# If we didn't perform a swap previously, exit
	
	add $s3, $zero, $zero	# clear the swapped boolean
	addi $s2, $zero, 0		# set up the inner loop counter
InnerLoop:
	add $s2, $s2, 4			# increment the loop counter (in bytes)
	blt $s1, $s2  OuterLoop	# If we have incremented past the number of elements, go back to OuterLoop
	add $t0, $s0, $s2		# add our loop value to the base address of the array
	lw $t1, -4($t0)			# $t1 = arr[i - 1]
	lw $t2, 0($t0)			# $t1 = arr[i]
	blt $t1, $t2, InnerLoop	# If arr[i] < arr[i - 1], go to beginning of loop
	sw $t2, -4($t0)			# Store arr[i - 1] in arr[i]
	sw $t1, 0($t0)			# Store arr[i] in arr[i - 1]
	addi $s3, $zero, 1		# Set the swapped boolean value, indicating we performed a swap
	j InnerLoop			# Jump back to the top of innner loop
	
Exit:
	
