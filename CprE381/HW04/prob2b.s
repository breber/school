.data
str1: .asciiz " Please enter a number >= 0 : "	# Store the prompt in data memory
str2: .asciiz "\n\nThe result is: "		# Store the result string in data memory
.text						# The following items will be in "text" memory
.globl main					# Put the address of main in global memory
main:						
jal prompt					# prompt the user for an int
move $t2, $v0					# copy the result from the user into $t2

jal prompt					# prompt the user for another int
move $t3, $v0					# copy the result from the user into $t3

sgt $t4, $t3, $t2				# store if $t3 > $t2
beq $t4, $zero, loop				# if $t3 < $t2, jump to loop
xor $t3, $t3, $t2
xor $t2, $t3, $t2				# swap $t3 and $t2 so that the lesser is in $t3
xor $t3, $t3, $t2				


loop:						
sge $t4, $zero, $t3				# store if $zero is greater or equal to $t3
bnez $t4, end					# if the loop counter is less than $zero jump to end
add $t0, $t0 , $t2				# add $t2 to $t0 and store it to $t0
addi $t3, $t3 , -1				# decrement the value the user inputted
j loop						# go back to loop

end:
li $v0, 4					# load the value 4 into $v0
la $a0, str2					# Load the address of the result string into $a0
syscall						# execute a system call, printing out the result string
li $v0, 1					# load the value 1 into $v0
move $a0, $t0					# copy the result of the loop into $a0
syscall						# execute a system call, printing out our integer

do_nothing:
li $v0, 10					# load the value 10 into $v0
syscall						# execute a system call, exiting the program


prompt:
li $v0, 4					# Load the value 4 into $v0
la $a0, str1					# Load the prompt into $a0
syscall						# Call the system function based on value
						# in $v0 --> 4 corresponds to print a string
li $v0, 5					# Load the value 5 into $v0
syscall						# make another system call --> 5 corresponds to read int
jr $ra						# return to caller
