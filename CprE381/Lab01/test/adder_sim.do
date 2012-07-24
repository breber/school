# Start up the simulation
vsim -novopt work.adder 

# Set binary strings to be hex values (not needed for now)
set binopt {-logic}
set hexopt {-literal -hex}

# Divide up the wave window so signals are well labelled
eval add wave -noupdate -divider {"Adder"}
eval add wave sim:/adder/*

# Force the clock value (default clock)
force -freeze sim:/adder/iclk 1 0, 0 {100 ns} -r 100


# Test 5 + 5
force -freeze sim:/adder/ia 5 0
force -freeze sim:/adder/ib 5 0
run 400ns

# Test 5 + (-5)
force -freeze sim:/adder/ia 5 0
force -freeze sim:/adder/ib -5 0
run 400ns

# Test (-5) + (-5)
force -freeze sim:/adder/ia -5 0
force -freeze sim:/adder/ib -5 0
run 400ns
