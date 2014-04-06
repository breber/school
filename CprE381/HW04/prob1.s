.data						# The following code will be in data memory
str1: .asciiz " Please enter a number >= 1 : "	# Store the prompt in data memory
str2: .asciiz "\n\n The result is: "		# Store the result string in data memory
.text						# The following items will be in "text" memory
.globl main					# Put the address of main in global memory

main:						
li $v0, 4					# Load the value 4 into $v0
la $a0, str1				# Load the prompt into $a0
syscall						# Call the system function based on value
							# in $v0 --> 4 corresponds to print a string
li $v0, 5                   # Load the value 5 into $v0
syscall						# make another system call --> 5 corresponds to read int
move $t1, $v0				# move the integer we just read into $t1
addi $t3, $zero, 1			# put the value 1 into $t3
slti $t2, $t1, 1			# store if our integer is less than 1
beq $t3, $t2, do_nothing	# if our integer is less than one, go to do_nothing
addi $t0, $t0, 0			# I dont understand the point of this... it adds 0 to
							# $t0 and stores it back into $t0...(i think it should be addi $t0, $zero, $zero)
loop:						
sge $t2, $t1, $t3			# store if our int is greater or equal to $t3
beq $t2, $zero, end			# if our int is less than $t3 jump to end
add $t0, $t0 , $t1			# add $t1 to $t0 and store it to $t0
addi $t1, $t1 , -1			# decrement the value the user inputted
j loop						# go back to loop

end:
li $v0, 4					# load the value 4 into $v0
la $a0, str2				# Load the address of the result string into $a0
syscall						# execute a system call, printing out the result string
li $v0, 1					# load the value 1 into $v0
move $a0, $t0				# copy the result of the loop into $a0
syscall						# execute a system call, printing out our integer

do_nothing:
li $v0, 10					# load the value 10 into $v0
syscall						# execute a system call, exiting the program
