# Start up the simulation
vsim -novopt work.two_to_one_mux

# Set binary strings to be hex values (not needed for now)
set binopt {-logic}
set hexopt {-literal -hex}

# Divide up the wave window so signals are well labelled
eval add wave -noupdate -divider {"2:1 Mux"}
eval add wave sim:/two_to_one_mux/*

# Test input A = 0, B = 1, Selection = 1 --> should get 1 as output
force -freeze sim:/two_to_one_mux/i_A 0 0
force -freeze sim:/two_to_one_mux/i_B 1 0
force -freeze sim:/two_to_one_mux/i_S 1 0
run 200ns

# Test input A = 0, B = 1, Selection = 0 --> should get 0 as output
force -freeze sim:/two_to_one_mux/i_A 0 0
force -freeze sim:/two_to_one_mux/i_B 1 0
force -freeze sim:/two_to_one_mux/i_S 0 0
run 200ns
