.data				# The following code will be in data memory
str1: .asciiz "The result is: "	# Store the result string in data memory
.text				# The following items will be in "text" memory
.globl main			# Put the address of main in global memory
main:				
addi $t0, $zero, 1		# place the value 1 into $t0
addi $t1, $zero, 0		# Place 0 into $t1

loop:				
slti $t2, $t0, 100		# store if our int is less than 100
beqz $t2, end			# if our int is not less than 100, jump to end
add $t1, $t1, $t0		# add $t1 to $t0 and store it to $t1
addi $t0, $t0 , 2		# add 2 to get the next odd number
j loop				# go back to loop

end:
li $v0, 4			# load the value 4 into $v0
la $a0, str1			# Load the address of the result string into $a0
syscall				# execute a system call, printing out the result string
li $v0, 1			# load the value 1 into $v0
move $a0, $t1			# copy the result of the loop into $a0
syscall				# execute a system call, printing out our integer
do_nothing:
li $v0, 10			# load the value 10 into $v0
syscall				# execute a system call, exiting the program
