# Start up the simulation
vsim -novopt work.quadratic 

# Set binary strings to be hex values (not needed for now)
set binopt {-logic}
set hexopt {-literal -hex}

# Divide up the wave window so signals are well labelled
eval add wave -noupdate -divider {"Quadratic"}
eval add wave sim:/quadratic/*

eval add wave -noupdate -divider {"g_mult1"}
eval add wave sim:/quadratic/g_mult1/*

eval add wave -noupdate -divider {"g_mult2"}
eval add wave sim:/quadratic/g_mult2/*

eval add wave -noupdate -divider {"g_mult3"}
eval add wave sim:/quadratic/g_mult3/*

eval add wave -noupdate -divider {"g_add1"}
eval add wave sim:/quadratic/g_add1/*

eval add wave -noupdate -divider {"g_add2"}
eval add wave sim:/quadratic/g_add2/*

# Force the clock value (default clock)
force -freeze sim:/quadratic/iclk 1 0, 0 {50 ns} -r 100

# Test input iX = 5
force -freeze sim:/quadratic/ix 5 0
run 400ns

# Test input iX = (-17)
force -freeze sim:/quadratic/ix -17 0
run 400ns
