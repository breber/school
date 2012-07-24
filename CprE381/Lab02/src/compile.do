vlib work

# Generic VHDL files
vcom -reportprogress 300 -work work and2.vhd
vcom -reportprogress 300 -work work inv.vhd
vcom -reportprogress 300 -work work or2.vhd
vcom -reportprogress 300 -work work tb_and2.vhd
vcom -reportprogress 300 -work work xor2.vhd

# Part 1 compilation
vcom -reportprogress 300 -work work P1/ones_compliment.vhd
vcom -reportprogress 300 -work work P1/ones_compliment_dataflow.vhd
vcom -reportprogress 300 -work work P1/tb_ones_compliment.vhd

# Part 2 compilation
vcom -reportprogress 300 -work work P2/two_to_one_mux.vhd
vcom -reportprogress 300 -work work P2/two_to_one_mux_nbit.vhd
vcom -reportprogress 300 -work work P2/two_to_one_mux_nbit_df.vhd
vcom -reportprogress 300 -work work P2/tb_two_to_one_mux.vhd

# Part 3 compilation
vcom -reportprogress 300 -work work P3/full_adder.vhd
vcom -reportprogress 300 -work work P3/full_adder_nbit.vhd
vcom -reportprogress 300 -work work P3/full_adder_nbit_df.vhd
vcom -reportprogress 300 -work work P3/tb_full_adder.vhd

# Part 4 compilation
vcom -reportprogress 300 -work work P4/add_sub_nbit.vhd
vcom -reportprogress 300 -work work P4/tb_add_sub.vhd
