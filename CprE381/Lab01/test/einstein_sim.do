# Start up the simulation
vsim -novopt work.einstein 

# Set binary strings to be hex values (not needed for now)
set binopt {-logic}
set hexopt {-literal -hex}

# Divide up the wave window so signals are well labelled
eval add wave -noupdate -divider {"Einstein"}
eval add wave sim:/einstein/*

eval add wave -noupdate -divider {"g_mult1"}
eval add wave sim:/einstein/g_mult1/*

eval add wave -noupdate -divider {"g_mult2"}
eval add wave sim:/einstein/g_mult2/*

# Force the clock value (default clock)
force -freeze sim:/einstein/iclk 1 0, 0 {50 ns} -r 100

# Test input 5
force -freeze sim:/einstein/im 5 0
run 400ns

# Test input -17
force -freeze sim:/einstein/im -17 0
run 200ns
