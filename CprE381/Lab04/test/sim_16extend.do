# Start up the simulation
vsim -novopt work.sixteen_to_32_extender 

# Set binary strings to be hex values (not needed for now)
set binopt {-logic}
set hexopt {-literal -hex}

# Divide up the wave window so signals are well labeled
eval add wave sim:/sixteen_to_32_extender/*


# Zero extend value
force -freeze sim:/sixteen_to_32_extender/i_In 1001001110010011 0
force -freeze sim:/sixteen_to_32_extender/i_S 0 0
run 200ns

# Sign extend value
force -freeze sim:/sixteen_to_32_extender/i_In 1001001110010011 0
force -freeze sim:/sixteen_to_32_extender/i_S 1 0
run 200ns

# Zero extend value
force -freeze sim:/sixteen_to_32_extender/i_In 0110101001101010 0
force -freeze sim:/sixteen_to_32_extender/i_S 0 0
run 200ns

# Sign extend value
force -freeze sim:/sixteen_to_32_extender/i_In 0110101001101010 0
force -freeze sim:/sixteen_to_32_extender/i_S 1 0
run 200ns
