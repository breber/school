vlib work

# Generic VHDL files
vcom -reportprogress 300 -work work dff.vhd
vcom -reportprogress 300 -work work tb_dff.vhd
vcom -reportprogress 300 -work work and2.vhd
vcom -reportprogress 300 -work work inv.vhd
vcom -reportprogress 300 -work work or2.vhd
vcom -reportprogress 300 -work work xor2.vhd

# Part 1 compilation
vcom -reportprogress 300 -work work P1/nbit_register.vhd
vcom -reportprogress 300 -work work P1/tb_nbit_register.vhd
vcom -reportprogress 300 -work work P1/five_to_thirtytwo_decoder.vhd
vcom -reportprogress 300 -work work P1/tb_five_to_thirtytwo_decoder.vhd
vcom -reportprogress 300 -work work P1/thirtytwo_to_one_mux_df.vhd
vcom -reportprogress 300 -work work P1/register_file.vhd

# Part 2 compilation
vcom -reportprogress 300 -work work P2/full_adder.vhd
vcom -reportprogress 300 -work work P2/full_adder_nbit.vhd
vcom -reportprogress 300 -work work P2/ones_compliment.vhd
vcom -reportprogress 300 -work work P2/two_to_one_mux.vhd
vcom -reportprogress 300 -work work P2/two_to_one_mux_nbit.vhd
vcom -reportprogress 300 -work work P2/add_sub_nbit.vhd
vcom -reportprogress 300 -work work P2/simple_proc.vhd
vcom -reportprogress 300 -work work P2/tb_simple_proc.vhd
