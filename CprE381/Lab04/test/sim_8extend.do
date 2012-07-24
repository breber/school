# Start up the simulation
vsim -novopt work.eight_to_32_extender 

# Set binary strings to be hex values (not needed for now)
set binopt {-logic}
set hexopt {-literal -hex}

# Divide up the wave window so signals are well labelled
eval add wave -noupdate -divider {"Quadratic"}
eval add wave sim:/eight_to_32_extender/*


# Zero extend value
force -freeze sim:/eight_to_32_extender/i_In 10010011 0
force -freeze sim:/eight_to_32_extender/i_S 0 0
run 200ns

# Sign extend value
force -freeze sim:/eight_to_32_extender/i_In 10010011 0
force -freeze sim:/eight_to_32_extender/i_S 1 0
run 200ns

# Zero extend value
force -freeze sim:/eight_to_32_extender/i_In 01101010 0
force -freeze sim:/eight_to_32_extender/i_S 0 0
run 200ns

# Sign extend value
force -freeze sim:/eight_to_32_extender/i_In 01101010 0
force -freeze sim:/eight_to_32_extender/i_S 1 0
run 200ns
