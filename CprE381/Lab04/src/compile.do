vlib work

# Generic VHDL files
vcom -reportprogress 300 -work work and2.vhd
vcom -reportprogress 300 -work work dff.vhd
vcom -reportprogress 300 -work work inv.vhd
vcom -reportprogress 300 -work work or2.vhd
vcom -reportprogress 300 -work work xor2.vhd
vcom -reportprogress 300 -work work mem.vhd

# Part 1 VHDL files
vcom -reportprogress 300 -work work P1/eight_to_32_extender.vhd
vcom -reportprogress 300 -work work P1/sixteen_to_32_extender.vhd

# Part 2 VHDL files
vcom -reportprogress 300 -work work P2/tb_dmem.vhd

# Part 3 VHDL files
vcom -reportprogress 300 -work work P3/add_sub_nbit.vhd
vcom -reportprogress 300 -work work P3/full_adder.vhd
vcom -reportprogress 300 -work work P3/full_adder_nbit.vhd
vcom -reportprogress 300 -work work P3/full_adder_nbit_df.vhd
vcom -reportprogress 300 -work work P3/nbit_register.vhd
vcom -reportprogress 300 -work work P3/register_file.vhd
vcom -reportprogress 300 -work work P3/five_to_thirtytwo_decoder.vhd
vcom -reportprogress 300 -work work P3/four_to_one_mux_nbit.vhd
vcom -reportprogress 300 -work work P3/thirtytwo_to_one_mux_df.vhd
vcom -reportprogress 300 -work work P3/ones_compliment.vhd
vcom -reportprogress 300 -work work P3/simple_proc.vhd
vcom -reportprogress 300 -work work P3/two_to_one_mux.vhd
vcom -reportprogress 300 -work work P3/two_to_one_mux_nbit.vhd
vcom -reportprogress 300 -work work P3/tb_simpleproc.vhd
