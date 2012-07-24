# Start up the simulation
vsim -novopt work.ones_complimenter_df

# Set binary strings to be hex values (not needed for now)
set binopt {-logic}
set hexopt {-literal -hex}

# Divide up the wave window so signals are well labelled
eval add wave -noupdate -divider {"Ones Complimenter Dataflow"}
eval add wave sim:/ones_complimenter_df/*

# Test input 01110111
force -freeze sim:/ones_complimenter_df/i_a 01110111 0
run 200ns

# Test input 10110101
force -freeze sim:/ones_complimenter_df/i_a 10110101 0
run 200ns
